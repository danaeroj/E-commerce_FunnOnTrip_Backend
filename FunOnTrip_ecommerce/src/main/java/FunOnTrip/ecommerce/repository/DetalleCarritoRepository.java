package FunOnTrip.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import FunOnTrip.ecommerce.model.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

	// Buscar todos los detalles de un carrito específico
	List<DetalleCarrito> findByCarritoIdCarrito(Integer carritoId);

	// Buscar si un producto ya está en el carrito
	Optional<DetalleCarrito> findByCarritoIdCarritoAndProductoIdProducto(Integer carritoId, Long productoId);
}

