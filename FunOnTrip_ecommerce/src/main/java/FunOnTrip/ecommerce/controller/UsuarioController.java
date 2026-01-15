package FunOnTrip.ecommerce.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import FunOnTrip.ecommerce.model.Rol;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.service.UsuarioService;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET - todos
    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // GET - por id
    @GetMapping("/{id}")
    public Usuario getUsuario(@PathVariable("id") Long id) {
        return usuarioService.getUsuarioById(id);
    }

    // POST - crear
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.createUsuario(usuario);
    }

    @GetMapping("/me")
    public Usuario me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
      // user.getUsername() = correo
      return usuarioService.getByEmail(user.getUsername());
    }

    @PatchMapping("/me/password")
    public Usuario cambiarPassword(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                                   @RequestBody UsuarioService.PasswordChangeRequest req) {
      Usuario u = usuarioService.getByEmail(user.getUsername());
      return usuarioService.updatePassword(u.getId(), req);
    }


    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable("id") Long id) {
        usuarioService.deleteUsuario(id);
    }

    // PATCH - cambiar SOLO rol
    // Ej: PATCH /api/usuarios/6/rol?rol=admin
    @PatchMapping("/{id}/rol")
    public Usuario actualizarRol(@PathVariable("id") Long id,
                                 @RequestParam("rol") Rol rol) {
        return usuarioService.actualizarRol(id, rol);
    }
    
        // PATCH - actualizar 
 // PATCH - actualizar campos parciales (nombre, telefono, etc.)
    @PatchMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable("id") Long id,
                                     @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }
    
 // PATCH - cambiar password con password anterior
 // Ej: PATCH /api/usuarios/6/password
 @PatchMapping("/{id}/password")
 public Usuario actualizarPassword(@PathVariable("id") Long id,
                                   @RequestBody UsuarioService.PasswordChangeRequest req) {
     return usuarioService.updatePassword(id, req);
 }


 // DTO interno (puede ir al final del controller o en paquete dto)
 public static class PasswordChangeRequest {
     public String currentPassword;
     public String newPassword;
 }

}
