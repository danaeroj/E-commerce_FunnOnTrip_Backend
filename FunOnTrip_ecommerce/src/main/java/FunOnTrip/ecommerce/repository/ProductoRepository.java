package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Obtener solo productos activos
    List<Producto> findByActivoTrue();

    // Obtener productos con stock disponible (> 0)
    List<Producto> findByStockGreaterThan(Integer stock);
}
