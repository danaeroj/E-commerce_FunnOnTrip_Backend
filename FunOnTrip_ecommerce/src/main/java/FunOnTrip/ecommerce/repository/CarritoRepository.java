package FunOnTrip.ecommerce.repository;

import java.util.Optional;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

	// Buscar carrito activo de un usuario
	Optional<Carrito> findByUsuarioIdUsuariosAndEstado(Long usuarioId, EstadoCarrito estado);

	// Buscar cualquier carrito de un usuario
	Optional<Carrito> findByUsuarioIdUsuarios(Long usuarioId);
}
