package FunOnTrip.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidad JPA: DetallePedido
 * Mapea la tabla "Detalle_pedidos".
 *
 * Clave:
 * - @JsonBackReference evita que al serializar el detalle vuelva a meter el Pedido completo (JSON infinito).
 */
@Entity
@Table(name = "Detalle_pedidos")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalle_pedidos")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Pedidos_idPedidos", nullable = false)
    @JsonBackReference
    private Pedido pedido;

    // Solo lectura para responder JSON sin cargar Pedido completo
    @Column(name = "Pedidos_idPedidos", insertable = false, updatable = false)
    private Integer pedidoId;

    // Sin entidad Producto (solo FK)
    @Column(name = "Producto_idProducto", nullable = false)
    private Integer productoId;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    public DetallePedido() {}

    // Getters / Setters
    public Integer getId() { return id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Integer getPedidoId() { return pedidoId; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
