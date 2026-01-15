package FunOnTrip.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

import FunOnTrip.ecommerce.model.Rol;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== GET ALL =====
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // ===== GET BY ID =====
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado con id: " + id));
    }

    // ===== CREATE =====
    @Transactional
    public Usuario createUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Body requerido");
        }
        if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "correoElectronico es requerido");
        }
        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico().trim())) {
            throw new ResponseStatusException(CONFLICT, "El correo ya está registrado");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "password es requerido");
        }

        // Defaults
        usuario.setCorreoElectronico(usuario.getCorreoElectronico().trim());
        usuario.setFechaRegistro(LocalDateTime.now());
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.USER);
        }

        // Encriptar password
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    // ===== UPDATE (completo, pero seguro con nulls) =====
    @Transactional
    public Usuario updateUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = getUsuarioById(id);

        if (usuarioActualizado == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Body requerido");
        }

        if (usuarioActualizado.getNombre() != null) {
            usuario.setNombre(usuarioActualizado.getNombre());
        }

        if (usuarioActualizado.getTelefono() != null) {
            usuario.setTelefono(usuarioActualizado.getTelefono());
        }

        if (usuarioActualizado.getRol() != null) {
            usuario.setRol(usuarioActualizado.getRol());
        }

        // Ojo: NO actualizo correo aquí para evitar problemas de unique.
        return usuarioRepository.save(usuario);
    }

    // ===== DELETE =====
    @Transactional
    public void deleteUsuario(Long id) {
        Usuario usuario = getUsuarioById(id);
        usuarioRepository.delete(usuario);
    }

    // ===== PATCH: actualizar SOLO rol =====
    @Transactional
    public Usuario actualizarRol(Long id, Rol rol) {
        Usuario usuario = getUsuarioById(id);
        if (rol == null) {
            throw new ResponseStatusException(BAD_REQUEST, "rol es requerido");
        }
        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }
    @Transactional
    public Usuario updatePassword(Long id, PasswordChangeRequest req) {
        if (req == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Body requerido");
        }
        if (req.currentPassword == null || req.currentPassword.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "currentPassword es requerido");
        }
        if (req.newPassword == null || req.newPassword.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "newPassword es requerido");
        }

        Usuario u = getUsuarioById(id);

        // Validar contraseña anterior (comparando contra la encriptada)
        if (!passwordEncoder.matches(req.currentPassword, u.getPassword())) {
            throw new ResponseStatusException(BAD_REQUEST, "Contraseña anterior incorrecta");
        }

        // Guardar nueva contraseña encriptada
        u.setPassword(passwordEncoder.encode(req.newPassword));
        return usuarioRepository.save(u);
    }

    /** DTO para cambio de password */
    public static class PasswordChangeRequest {
        public String currentPassword;
        public String newPassword;
    }
    
}
