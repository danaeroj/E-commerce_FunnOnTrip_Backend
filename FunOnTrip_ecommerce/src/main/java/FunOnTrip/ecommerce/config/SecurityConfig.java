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

                  			    // deja login/register según tu proyecto
                   .requestMatchers("/api/auth/**").permitAll()

                    // permitir leer carritos (si lo quieres así)
                   .requestMatchers(HttpMethod.GET, "/api/carritos/**").authenticated()

                    // permitir agregar detalle al carrito (POST)
                    .requestMatchers(HttpMethod.POST, "/api/detalles-carrito/**").hasRole("ADMIN")
                   // o si también lo pueden hacer USER:
                   // .requestMatchers(HttpMethod.POST, "/api/detalles-carrito/**").authenticated()


                    // auth público
                    .requestMatchers("/api/auth/**").permitAll()

                    // crear usuario público (opcional)
                    .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                    .requestMatchers("/api/carritos/**").permitAll()
                    
                    .requestMatchers("/api/carritos/**").permitAll()
                    
                    // contacto: crear público (form)
                    .requestMatchers(HttpMethod.POST, "/api/contacto").permitAll()

                    // productos: GET público
                    .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()

                    // productos: cambios solo admin
                    .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/productos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                    // pedidos
                    .requestMatchers(HttpMethod.DELETE, "/api/pedidos/**").hasRole("ADMIN")
                    .requestMatchers("/api/pedidos/**").authenticated()

                    // carritos: normalmente auth
                    .requestMatchers("/api/carritos/**").authenticated()

                    // contactos admin para ver/atender/borrar
                    .requestMatchers("/api/contacto/**").hasRole("ADMIN")

                    // todo lo demás
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
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
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