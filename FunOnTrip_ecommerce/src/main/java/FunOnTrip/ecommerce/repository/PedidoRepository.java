package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Trae pedidos por usuario usando FK real en BD
    @Query(value = "SELECT * FROM Pedidos WHERE Usuarios_idUsuarios = :usuarioId ORDER BY fecha_pedido DESC",
            nativeQuery = true)
    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(Integer usuarioId);

    // Trae un pedido con detalles (si tu relación es Pedido->detalles lazy)
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles WHERE p.idPedidos = :id")
    Optional<Pedido> findByIdWithDetalles(@Param("id") Integer id);
}
