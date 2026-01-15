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

import FunOnTrip.ecommerce.security.AuthController.RegisterRequest;

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

    public Usuario getByEmail(String email) {
    	  return usuarioRepository.findByCorreoElectronico(email)
    	    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado"));
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
    
    @Transactional
    public Usuario register(RegisterRequest req) {
      Usuario u = new Usuario();
      u.setNombre(req.nombre);
      u.setCorreoElectronico(req.correoElectronico);
      u.setPassword(req.password);
      u.setTelefono(req.telefono);
      return createUsuario(u); // usa tu validación + encriptación
    }

    @Transactional(readOnly = true)
    public Usuario getByEmail(String email) {
      return usuarioRepository.findByCorreoElectronico(email)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado: " + email));
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
      Usuario u = getUsuarioById(id);

      if (req == null || req.currentPassword == null || req.newPassword == null) {
        throw new ResponseStatusException(BAD_REQUEST, "Body requerido");
      }
      if (!passwordEncoder.matches(req.currentPassword, u.getPassword())) {
        throw new ResponseStatusException(BAD_REQUEST, "Contraseña anterior incorrecta");
      }
      if (req.newPassword.isBlank()) {
        throw new ResponseStatusException(BAD_REQUEST, "newPassword es requerido");
      }

      u.setPassword(passwordEncoder.encode(req.newPassword));
      return usuarioRepository.save(u);
    }

    public static class PasswordChangeRequest {
      public String currentPassword;
      public String newPassword;
    }

    /** DTO para cambio de password */
    public static class PasswordChangeRequest1 {
        public String currentPassword;
        public String newPassword;
    }
    
}
