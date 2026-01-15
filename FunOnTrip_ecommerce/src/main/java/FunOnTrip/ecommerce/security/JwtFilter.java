package FunOnTrip.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(TokenService tokenService, CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();

        try {
            if (!tokenService.isValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = tokenService.extractSubject(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // ✅ OBTENER ROL DEL TOKEN
                String roleFromToken = tokenService.extractRole(token);
                List<GrantedAuthority> authorities;
                
                if (roleFromToken != null) {
                    // Asegurar prefijo ROLE_
                    String authority = roleFromToken.startsWith("ROLE_") 
                        ? roleFromToken 
                        : "ROLE_" + roleFromToken;
                    
                    authorities = Collections.singletonList(
                        new SimpleGrantedAuthority(authority)
                    );
                    
                    System.out.println("DEBUG: Rol del token: " + authority);
                } else {
                    // Backup: obtener de la BD
                    org.springframework.security.core.userdetails.UserDetails userDetails = 
                        userDetailsService.loadUserByUsername(email);
                    authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
                }

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception ex) {
            // Continuar sin autenticar
        }

        filterChain.doFilter(request, response);
    }
}