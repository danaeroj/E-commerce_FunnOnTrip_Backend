package FunOnTrip.ecommerce.service;

package org.generation.ecommerce.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import FunOnTrip.ecommerce.model.DetallePedido;
import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.model.Producto;
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
     * - setProducto(producto) conecta con la FK Detalle_pedidos.Producto_idProducto.
     *
     * Nota: en un e-commerce real, normalmente los detalles se crean dentro del flujo de "crearPedido".
     * Se expone aquí por requerimiento de CRUD completo.
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

        Producto producto = entityManager.find(Producto.class, productoId);
        if (producto == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El producto no existe: " + productoId);
        }

        BigDecimal precioUnitario = producto.getPrecio().setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(cantidad)).setScale(2, RoundingMode.HALF_UP);

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotal);

        return detallePedidoRepository.save(detalle);
    }

    @Transactional
    public DetallePedido updateCantidad(Integer detalleId, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }

        DetallePedido detalle = getById(detalleId);

        BigDecimal precioUnitario = detalle.getPrecioUnitario().setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(nuevaCantidad)).setScale(2, RoundingMode.HALF_UP);

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
