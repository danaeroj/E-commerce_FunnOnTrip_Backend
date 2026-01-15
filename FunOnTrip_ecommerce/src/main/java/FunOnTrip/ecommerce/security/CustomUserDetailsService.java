package FunOnTrip.ecommerce.security;

import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correoElectronico) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correoElectronico));

        String role = "ROLE_" + u.getRol().name().toUpperCase(); // ROLE_ADMIN / ROLE_USER

        
        return new org.springframework.security.core.userdetails.User(
                u.getCorreoElectronico(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
