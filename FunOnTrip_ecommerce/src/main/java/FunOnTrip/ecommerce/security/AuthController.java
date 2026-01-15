package FunOnTrip.ecommerce.security;

import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {

        if (req == null) throw new ResponseStatusException(BAD_REQUEST, "Body requerido");
        if (req.correoElectronico == null || req.correoElectronico.isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "correoElectronico es requerido");
        if (req.password == null || req.password.isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "password es requerido");

        String email = req.correoElectronico.trim();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, req.password)
            );
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(UNAUTHORIZED, "Credenciales inválidas");
        } catch (DisabledException e) {
            throw new ResponseStatusException(FORBIDDEN, "Usuario deshabilitado");
        }

        Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuario no encontrado"));

        String token = tokenService.createToken(usuario); // <- aquí debe regresar JWT real

        LoginResponse resp = new LoginResponse();
        resp.usuarioId = usuario.getId();
        resp.correoElectronico = usuario.getCorreoElectronico();
        resp.rol = (usuario.getRol() != null) ? usuario.getRol().name() : null;
        resp.token = token;
        resp.tipo = "Bearer";

        return ResponseEntity.ok(resp);
    }

    // ========= DTOs internos =========
    public static class LoginRequest {
        public String correoElectronico;
        public String password;
        public LoginRequest() {}
    }

    public static class LoginResponse {
        public Long usuarioId;
        public String correoElectronico;
        public String rol;
        public String token;
        public String tipo;
    }
}
