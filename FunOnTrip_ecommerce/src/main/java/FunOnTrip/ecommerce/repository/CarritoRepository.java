package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    // IMPORTANTE: Usa Carrito.EstadoCarrito, no EstadoCarrito solo
    Optional<Carrito> findByUsuario_IdAndEstado(Long usuarioId, Carrito.EstadoCarrito estado);
}
