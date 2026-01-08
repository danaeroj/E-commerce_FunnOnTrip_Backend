package FunOnTrip.ecommerce.repository;

import java.util.Optional;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
	
	// Buscar el carrito activo de un usuario
	Optional<Carrito> findByUsuarioIdAndEstado(Long usuarioId, EstadoCarrito estado);
	
	// Buscar todos los carritos de un usuario
	Optional<Carrito> findByUsuarioId(Long usuarioId);
}
