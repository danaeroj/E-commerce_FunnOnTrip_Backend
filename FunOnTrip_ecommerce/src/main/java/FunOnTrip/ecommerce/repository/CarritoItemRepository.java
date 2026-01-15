package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {

  Optional<CarritoItem> findByCarrito_IdAndProductoId(Integer carritoId, Long productoId);

  List<CarritoItem> findByCarrito_Id(Integer carritoId);
}
