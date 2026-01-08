package FunOnTrip.ecommerce.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import FunOnTrip.ecommerce.model.DetallePedido;
import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.model.Usuario;
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

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * IVA definido por tu BD como 16% (comentario en create.sql).
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
     * Flujo de creación de pedidos:
     * - Valida usuario existente (FK Usuarios_idUsuarios).
     * - Valida productos existentes (FK Producto_idProducto).
     * - Calcula subtotal, impuestos y total.
     * - Persiste Pedido y sus DetallePedido en una sola transacción.
     *
     * Conexiones clave:
     * - Pedido.setUsuario(usuario) conecta con Usuarios.
     * - detalle.setProducto(producto) conecta con Producto.
     * - pedido.addDetalle(detalle) conecta Pedido con DetallePedido y asegura la FK Pedidos_idPedidos.
     */
    @Transactional
    public Pedido crearPedido(Integer usuarioId, String metodoPago, List<ItemPedido> items) {

        if (usuarioId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "usuarioId es requerido");
        }
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "El pedido debe incluir al menos 1 producto");
        }

        Usuario usuario = entityManager.find(Usuario.class, usuarioId);
        if (usuario == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El usuario no existe: " + usuarioId);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setMetodoPago(metodoPago);
        pedido.setEstado(Pedido.Estado.pendiente);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedido item : items) {
            if (item == null || item.productoId == null || item.cantidad == null) {
                throw new ResponseStatusException(BAD_REQUEST, "Cada item requiere productoId y cantidad");
            }
            if (item.cantidad <= 0) {
                throw new ResponseStatusException(BAD_REQUEST, "La cantidad debe ser mayor a 0");
            }

            Producto producto = entityManager.find(Producto.class, item.productoId);
            if (producto == null) {
                throw new ResponseStatusException(BAD_REQUEST, "El producto no existe: " + item.productoId);
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotalItem = precioUnitario
                    .multiply(new BigDecimal(item.cantidad))
                    .setScale(2, RoundingMode.HALF_UP);

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad);
            detalle.setPrecioUnitario(precioUnitario.setScale(2, RoundingMode.HALF_UP));
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
     * DTO interno (no archivo extra) para crear pedidos desde el Controller.
     */
    public static class ItemPedido {
        public Integer productoId;
        public Integer cantidad;

        public ItemPedido() {
        }

        public ItemPedido(Integer productoId, Integer cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }
    }
}
