package FunOnTrip.ecommerce.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import FunOnTrip.ecommerce.model.DetallePedido;
import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.repository.DetallePedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<DetallePedido> getAll() {
        return detallePedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DetallePedido getById(Integer id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "DetallePedido no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<DetallePedido> getByPedido(Integer pedidoId) {
        return detallePedidoRepository.findAllByPedidoId(pedidoId);
    }

    /**
     * CRUD: Crear detalle.
     *
     * Conexiones:
     * - setPedido(pedido) conecta con la FK Detalle_pedidos.Pedidos_idPedidos.
     * - setProductoId(productoId) conecta con la FK Detalle_pedidos.Producto_idProducto (sin depender de Producto.java).
     *
     * El precio unitario se obtiene directamente desde la tabla Producto con SQL nativo.
     */
    @Transactional
    public DetallePedido create(Integer pedidoId, Integer productoId, Integer cantidad) {

        if (pedidoId == null || productoId == null || cantidad == null) {
            throw new ResponseStatusException(BAD_REQUEST, "pedidoId, productoId y cantidad son requeridos");
        }
        if (cantidad <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }

        Pedido pedido = entityManager.find(Pedido.class, pedidoId);
        if (pedido == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El pedido no existe: " + pedidoId);
        }

        BigDecimal precioUnitario = obtenerPrecioProducto(productoId)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal subtotal = precioUnitario
                .multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);

        // IMPORTANTE: tu entidad DetallePedido debe tener este setter.
        detalle.setProductoId(productoId);

        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotal);

        return detallePedidoRepository.save(detalle);
    }

    /**
     * Obtiene el precio (DECIMAL) desde la tabla Producto sin usar la entidad Producto.
     * Esto evita depender del modelo de tu compañera y evita el error de Double.setScale().
     */
    private BigDecimal obtenerPrecioProducto(Integer productoId) {
        try {
            Object result = entityManager.createNativeQuery(
                            "SELECT precio FROM Producto WHERE idProducto = :id")
                    .setParameter("id", productoId)
                    .getSingleResult();

            if (result == null) {
                throw new ResponseStatusException(BAD_REQUEST, "El producto no existe: " + productoId);
            }

            if (result instanceof BigDecimal bd) {
                return bd;
            }
            if (result instanceof Number n) {
                return BigDecimal.valueOf(n.doubleValue());
            }

            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,
                    "Tipo de dato inesperado para precio en Producto: " + result.getClass().getName());

        } catch (NoResultException e) {
            throw new ResponseStatusException(BAD_REQUEST, "El producto no existe: " + productoId);
        }
    }

    @Transactional
    public DetallePedido updateCantidad(Integer detalleId, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }

        DetallePedido detalle = getById(detalleId);

        BigDecimal precioUnitario = detalle.getPrecioUnitario().setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = precioUnitario
                .multiply(BigDecimal.valueOf(nuevaCantidad))
                .setScale(2, RoundingMode.HALF_UP);

        detalle.setCantidad(nuevaCantidad);
        detalle.setSubtotal(subtotal);

        return detallePedidoRepository.save(detalle);
    }

    @Transactional
    public void delete(Integer detalleId) {
        if (!detallePedidoRepository.existsById(detalleId)) {
            throw new ResponseStatusException(NOT_FOUND, "DetallePedido no encontrado: " + detalleId);
        }
        detallePedidoRepository.deleteById(detalleId);
    }
}
