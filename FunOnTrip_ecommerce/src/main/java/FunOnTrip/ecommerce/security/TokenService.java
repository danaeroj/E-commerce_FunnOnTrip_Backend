package FunOnTrip.ecommerce.security;

import FunOnTrip.ecommerce.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Service
public class TokenService {

    private final SecretKey key;
    private final long expirationMinutes;

    public TokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMinutes:1440}") long expirationMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String createToken(Usuario usuario) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationMinutes * 60);

        // 🔥 INCLUIR ROL en el token
        Map<String, Object> claims = new HashMap<>();
        
        if (usuario.getRol() != null) {
            String rol = usuario.getRol().name(); // "ADMIN" o "USER"
            claims.put("rol", rol);
            System.out.println("🔥 TokenService: Token creado con rol = " + rol);
        }

        return Jwts.builder()
                .setClaims(claims) // 🔥 ESTO AGREGA EL ROL
                .setSubject(usuario.getCorreoElectronico())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        try {
            Claims claims = parseClaims(token);
            return (String) claims.get("rol");
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}