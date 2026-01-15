package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Rol;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario getUsuario(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.createUsuario(usuario);
    }

    @GetMapping("/me")
    public Usuario me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        return usuarioService.getByEmail(user.getUsername());
    }

    @PatchMapping("/me/password")
    public Usuario cambiarPassword(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestBody UsuarioService.PasswordChangeRequest req) {

        Usuario u = usuarioService.getByEmail(user.getUsername());
        return usuarioService.updatePassword(u.getId(), req);
    }

    @PatchMapping("/{id}/rol")
    public Usuario actualizarRol(@PathVariable Long id, @RequestParam Rol rol) {
        return usuarioService.actualizarRol(id, rol);
    }

    @PatchMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
    }
}

