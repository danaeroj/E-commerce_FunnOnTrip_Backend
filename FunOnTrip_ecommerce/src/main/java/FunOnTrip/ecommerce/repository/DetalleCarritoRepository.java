package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

    List<DetalleCarrito> findByCarrito_Id(Long carritoId);

    Optional<DetalleCarrito> findByCarrito_IdAndProducto_Id(
            Long carritoId,
            Long productoId
    );
}

