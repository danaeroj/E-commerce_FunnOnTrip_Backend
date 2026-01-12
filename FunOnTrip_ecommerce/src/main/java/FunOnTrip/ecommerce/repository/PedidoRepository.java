	package FunOnTrip.ecommerce.repository;


import FunOnTrip.ecommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository de Pedido.
 *
 * Permite CRUD básico y consultas.
 * Nota: usamos una consulta native para "por usuario" porque evita depender del nombre del campo id en Usuario.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    /**
     * Trae un pedido con sus detalles usando JOIN FETCH para evitar LazyInitialization en escenarios sin OpenSessionInView.
     * Relación: Pedido (1) -> DetallePedido (N)
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles WHERE p.id = :id")
    Optional<Pedido> findByIdWithDetalles(@Param("id") Integer id);

    /**
     * Lista pedidos por usuario usando la columna FK de BD: Usuarios_idUsuarios.
     */
    @Query(value = "SELECT * FROM Pedidos WHERE Usuarios_idUsuarios = :usuarioId ORDER BY fecha_pedido DESC", nativeQuery = true)
    List<Pedido> findAllByUsuarioId(@Param("usuarioId") Integer usuarioId);
}
