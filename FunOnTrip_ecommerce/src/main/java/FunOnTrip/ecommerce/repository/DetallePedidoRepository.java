package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository de DetallePedido.
 *
 * Permite CRUD básico y consultas.
 * La consulta por pedido usa la FK Detalle_pedidos.Pedidos_idPedidos.
 */
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    @Query(value = "SELECT * FROM Detalle_pedidos WHERE Pedidos_idPedidos = :pedidoId", nativeQuery = true)
    List<DetallePedido> findAllByPedidoId(@Param("pedidoId") Integer pedidoId);
}
