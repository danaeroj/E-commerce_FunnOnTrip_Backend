package FunOnTrip.ecommerce.security;

import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.UsuarioRepository;
import FunOnTrip.ecommerce.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
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
  private final UsuarioService usuarioService; // ✅

  public AuthController(AuthenticationManager authenticationManager,
                        UsuarioRepository usuarioRepository,
                        TokenService tokenService,
                        UsuarioService usuarioService) {
    this.authenticationManager = authenticationManager;
    this.usuarioRepository = usuarioRepository;
    this.tokenService = tokenService;
    this.usuarioService = usuarioService;
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
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, req.password));
    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(UNAUTHORIZED, "Credenciales inválidas");
    }

    Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
      .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuario no encontrado"));

    String token = tokenService.createToken(usuario);

    LoginResponse resp = new LoginResponse();
    resp.usuarioId = usuario.getId();
    resp.correoElectronico = usuario.getCorreoElectronico();
    resp.rol = (usuario.getRol() != null) ? usuario.getRol().name() : null;
    resp.token = token;
    resp.tipo = "Bearer";

    return ResponseEntity.ok(resp);
  }

  // ✅ REGISTER -> regresa token igual que login
  @PostMapping("/register")
  public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest req) {
    if (req == null) throw new ResponseStatusException(BAD_REQUEST, "Body requerido");

    Usuario nuevo = usuarioService.register(req);

    String token = tokenService.createToken(nuevo);

    LoginResponse resp = new LoginResponse();
    resp.usuarioId = nuevo.getId();
    resp.correoElectronico = nuevo.getCorreoElectronico();
    resp.rol = (nuevo.getRol() != null) ? nuevo.getRol().name() : null;
    resp.token = token;
    resp.tipo = "Bearer";

    return ResponseEntity.status(CREATED).body(resp);
  }

  public static class LoginRequest {
    public String correoElectronico;
    public String password;
  }

  public static class RegisterRequest {
    public String nombre;
    public String correoElectronico;
    public String password;
    public String telefono;
  }

  public static class LoginResponse {
    public Long usuarioId;
    public String correoElectronico;
    public String rol;
    public String token;
    public String tipo;
  }
}
