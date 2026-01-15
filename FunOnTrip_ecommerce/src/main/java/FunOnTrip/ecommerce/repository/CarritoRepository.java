package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

    List<DetalleCarrito> findByCarrito_IdCarrito(Integer carritoId);

    Optional<DetalleCarrito> findByCarrito_IdCarritoAndProducto_IdProducto(
            Integer carritoId,
            Long productoId
    );
}

