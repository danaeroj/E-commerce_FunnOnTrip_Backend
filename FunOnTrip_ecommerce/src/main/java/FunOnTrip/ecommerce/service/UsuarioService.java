package FunOnTrip.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FunOnTrip.ecommerce.model.Rol;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por ID
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Crear usuario
    public Usuario createUsuario(Usuario usuario) {

        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setRol(Rol.user); // rol por defecto

        return usuarioRepository.save(usuario);
    }

    // 🔹 Actualizar usuario
    public Usuario updateUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = getUsuarioById(id);

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setRol(usuarioActualizado.getRol());

        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void deleteUsuario(Long id) {
        Usuario usuario = getUsuarioById(id);
        usuarioRepository.delete(usuario);
    }
}
