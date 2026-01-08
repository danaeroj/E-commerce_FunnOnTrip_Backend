package FunOnTrip.ecommerce.service;

import FunOnTrip.ecommerce.model.DetallePedido;
import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    /**
     * IVA definido por tu BD como 16%.
     */
    private static final BigDecimal IVA = new BigDecimal("0.16");

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido getById(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Pedido getByIdWithDetalles(Integer id) {
        return pedidoRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pedido> getByUsuario(Integer usuarioId) {
        return pedidoRepository.findAllByUsuarioId(usuarioId);
    }

    /**
     * Crea un pedido con detalles.
     *
     * En esta versión el flujo no depende de entidades Usuario/Producto,
     * solo usa usuarioId y productoId para no bloquear el avance del equipo.
     */
    @Transactional
    public Pedido crearPedido(Integer usuarioId, String metodoPago, List<ItemPedido> items) {

        if (usuarioId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "usuarioId es requerido");
        }
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "El pedido debe incluir al menos 1 producto");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setMetodoPago(metodoPago);
        pedido.setEstado(Pedido.Estado.pendiente);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedido item : items) {
            if (item == null || item.productoId == null || item.cantidad == null || item.precioUnitario == null) {
                throw new ResponseStatusException(BAD_REQUEST, "Cada item requiere productoId, cantidad y precioUnitario");
            }
            if (item.cantidad <= 0) {
                throw new ResponseStatusException(BAD_REQUEST, "La cantidad debe ser mayor a 0");
            }

            BigDecimal precioUnitario = item.precioUnitario.setScale(2, RoundingMode.HALF_UP);
            BigDecimal subtotalItem = precioUnitario
                    .multiply(BigDecimal.valueOf(item.cantidad))
                    .setScale(2, RoundingMode.HALF_UP);

            DetallePedido detalle = new DetallePedido();
            // ESTE MÉTODO DEBE EXISTIR EN DetallePedido.java
            detalle.setProductoId(item.productoId);
            detalle.setCantidad(item.cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotalItem);

            pedido.addDetalle(detalle);
            subtotal = subtotal.add(subtotalItem);
        }

        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal impuestos = subtotal.multiply(IVA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(impuestos).setScale(2, RoundingMode.HALF_UP);

        pedido.setSubtotal(subtotal);
        pedido.setImpuestos(impuestos);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarEstado(Integer pedidoId, Pedido.Estado nuevoEstado) {
        if (nuevoEstado == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El estado es requerido");
        }

        Pedido pedido = getById(pedidoId);
        pedido.setEstado(nuevoEstado);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarMetodoPago(Integer pedidoId, String metodoPago) {
        Pedido pedido = getById(pedidoId);
        pedido.setMetodoPago(metodoPago);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void delete(Integer pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new ResponseStatusException(NOT_FOUND, "Pedido no encontrado: " + pedidoId);
        }
        pedidoRepository.deleteById(pedidoId);
    }

    /**
     * DTO interno para recibir items del pedido.
     * precioUnitario se usa temporalmente mientras Producto no esté integrado.
     */
    public static class ItemPedido {
        public Integer productoId;
        public Integer cantidad;
        public BigDecimal precioUnitario;

        public ItemPedido() {}

        public ItemPedido(Integer productoId, Integer cantidad, BigDecimal precioUnitario) {
            this.productoId = productoId;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }
    }
}
