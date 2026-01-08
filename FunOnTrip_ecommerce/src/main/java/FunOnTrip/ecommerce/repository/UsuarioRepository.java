package FunOnTrip.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import FunOnTrip.ecommerce.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por correo (login / validación)
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    // Verificar si ya existe un correo (registro)
    boolean existsByCorreoElectronico(String correoElectronico);
}
