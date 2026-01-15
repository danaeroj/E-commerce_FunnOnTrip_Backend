package FunOnTrip.ecommerce.config;

import FunOnTrip.ecommerce.security.JwtFilter;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtFilter jwtFilter;

  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth

        // ===== PUBLIC =====
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()     // registro sin token (si quieres)
        .requestMatchers(HttpMethod.POST, "/api/contactos").permitAll()    // contacto público
        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()  // ver productos público

        // ===== ADMIN ONLY =====
        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/productos/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

        .requestMatchers(HttpMethod.GET, "/api/contactos/**").hasRole("ADMIN")
        // Si decides NO borrar contactos, NO pongas DELETE aquí.

        .requestMatchers(HttpMethod.GET, "/api/pedidos").hasRole("ADMIN")               // ver todos
        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/estado-final").hasRole("ADMIN")
        // Si quisieras borrar pedidos: .requestMatchers(HttpMethod.DELETE, "/api/pedidos/**").hasRole("ADMIN")

        // ===== AUTHENTICATED =====
        .requestMatchers("/api/carritos/**").authenticated()
        .requestMatchers(HttpMethod.POST, "/api/pedidos").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/pedidos/**").authenticated()
        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/**").authenticated()

        // ===== DEFAULT =====
        .anyRequest().authenticated()
      )
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOriginPatterns(List.of("*"));
    cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    cfg.setAllowedHeaders(List.of("*"));
    cfg.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }
}
