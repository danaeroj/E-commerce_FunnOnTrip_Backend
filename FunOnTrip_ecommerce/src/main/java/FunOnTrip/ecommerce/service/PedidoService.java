package FunOnTrip.ecommerce.service;

import FunOnTrip.ecommerce.model.DetallePedido;
import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.model.Producto;
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
    private final ProductoService productoService;

    // IVA 16%
    private static final BigDecimal IVA = new BigDecimal("0.16");
    private static final int SCALE = 2;

    public PedidoService(PedidoRepository pedidoRepository, ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
    }

    // Mejor como readOnly para queries
    @Transactional(readOnly = true)
    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido getByIdWithDetalles(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(BAD_REQUEST, "id es requerido");
        }
        return pedidoRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido no encontrado"));
    }

    @Transactional(readOnly = true)
    public Pedido getById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(BAD_REQUEST, "id es requerido");
        }
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido no encontrado"));
    }

    // Método para /api/pedidos/usuario/{usuarioId}
    @Transactional(readOnly = true)
    public List<Pedido> getByUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "usuarioId es requerido");
        }
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId);
    }

    /**
     * Crea un pedido con detalles.
     *
     * No depende de entidad Usuario; usa usuarioId y productoId.
     */
    @Transactional
    public Pedido crearPedido(Integer usuarioId, String metodoPago, List<ItemPedido> items) {

        if (usuarioId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "usuarioId es requerido");
        }
        if (metodoPago == null || metodoPago.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "metodoPago es requerido");
        }
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "El pedido debe incluir al menos 1 producto");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setMetodoPago(metodoPago.trim());
        pedido.setEstado(Pedido.Estado.pendiente);

        BigDecimal subtotal = BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);

        for (ItemPedido item : items) {
            validarItem(item);

            BigDecimal precioUnitario = resolverPrecioUnitario(item);

            BigDecimal subtotalItem = precioUnitario
                    .multiply(BigDecimal.valueOf(item.cantidad))
                    .setScale(SCALE, RoundingMode.HALF_UP);

            DetallePedido detalle = new DetallePedido();
            detalle.setProductoId(item.productoId);
            detalle.setCantidad(item.cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotalItem);

            pedido.addDetalle(detalle);
            subtotal = subtotal.add(subtotalItem).setScale(SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal impuestos = subtotal.multiply(IVA).setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(impuestos).setScale(SCALE, RoundingMode.HALF_UP);

        pedido.setSubtotal(subtotal);
        pedido.setImpuestos(impuestos);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarEstado(Integer pedidoId, Pedido.Estado nuevoEstado) {
        if (pedidoId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "pedidoId es requerido");
        }
        if (nuevoEstado == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El estado es requerido");
        }

        Pedido pedido = getById(pedidoId);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarMetodoPago(Integer pedidoId, String metodoPago) {
        if (pedidoId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "pedidoId es requerido");
        }
        if (metodoPago == null || metodoPago.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "metodoPago es requerido");
        }

        Pedido pedido = getById(pedidoId);
        pedido.setMetodoPago(metodoPago.trim());
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void delete(Integer pedidoId) {
        if (pedidoId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "pedidoId es requerido");
        }
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new ResponseStatusException(NOT_FOUND, "Pedido no encontrado: " + pedidoId);
        }
        pedidoRepository.deleteById(pedidoId);
    }

    // ================= Helpers =================

    private void validarItem(ItemPedido item) {
        if (item == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Cada item es requerido");
        }
        if (item.productoId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Cada item requiere productoId");
        }
        if (item.cantidad == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Cada item requiere cantidad");
        }
        if (item.cantidad <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }
        if (item.precioUnitario != null && item.precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(BAD_REQUEST, "precioUnitario no puede ser negativo");
        }
    }

    private BigDecimal resolverPrecioUnitario(ItemPedido item) {
        BigDecimal precioUnitario;

        // Si llega desde front lo aceptamos (debug), si no, lo calculamos desde Producto
        if (item.precioUnitario != null) {
            precioUnitario = item.precioUnitario;
        } else {
            Producto prod = productoService.obtenerProductoPorId(item.productoId.longValue());
            if (prod == null) {
                throw new ResponseStatusException(NOT_FOUND, "Producto no encontrado: " + item.productoId);
            }
            if (prod.getPrecio() == null) {
                throw new ResponseStatusException(INTERNAL_SERVER_ERROR,
                        "El producto no tiene precio: " + item.productoId);
            }

            // Si precio es Double/double:
            precioUnitario = BigDecimal.valueOf(prod.getPrecio());

            // Si tu Producto.getPrecio() fuera BigDecimal, sería:
            // precioUnitario = prod.getPrecio();
        }

        return precioUnitario.setScale(SCALE, RoundingMode.HALF_UP);
    }

    // ================= DTO =================

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
