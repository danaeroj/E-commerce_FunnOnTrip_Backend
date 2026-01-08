package FunOnTrip.ecommerce.model;


import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad JPA: DetallePedido
 *
 * Mapea la tabla "Detalle_pedidos" (plural en BD) a la clase "DetallePedido" (singular en Java).
 *
 * Conexiones:
 * - Se conecta con Pedido mediante la FK "Pedidos_idPedidos" (ManyToOne).
 * - Se conecta con Producto mediante la FK "Producto_idProducto" (ManyToOne).
 */
@Entity
@Table(name = "Detalle_pedidos")
public class DetallePedido {

    /**
     * PK de la tabla Detalle_pedidos: idDetalle_pedidos (AUTO_INCREMENT).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalle_pedidos")
    private Integer id;

    /**
     * Relación ManyToOne con Pedido.
     * Aquí se conecta con la tabla Pedidos usando la FK "Pedidos_idPedidos".
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Pedidos_idPedidos", nullable = false)
    private Pedido pedido;

    /**
     * FK expuesta solo para lectura (útil para responder JSON sin cargar Pedido).
     */
    @Column(name = "Pedidos_idPedidos", insertable = false, updatable = false)
    private Integer pedidoId;

    /**
     * Relación ManyToOne con Producto.
     * Aquí se conecta con la tabla Producto usando la FK "Producto_idProducto".
     */
  /**  
   * @ManyToOne(fetch = FetchType.LAZY, optional = false)
   * @JoinColumn(name = "Producto_idProducto", nullable = false)
   * private Producto producto;
    */
    
  

    /**
     * FK expuesta solo para lectura (útil para responder JSON sin cargar Producto).
     */
    @Column(name = "Producto_idProducto", nullable = false)
    private Integer productoId;


    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    public DetallePedido() {
    }


    
    /* =========================
       Getters / Setters
       ========================= */

    public Integer getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    /**
     * Parte clave de la conexión con Pedido.
     * Cuando se setea, JPA usa esto para guardar la FK Pedidos_idPedidos.
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
    
   /** public Producto getProducto() {
        return producto;
    }

    /**
     * Parte clave de la conexión con Producto.
     * Cuando se setea, JPA usa esto para guardar la FK Producto_idProducto.
     */
   /** public void setProducto(Producto producto) {
        this.producto = producto;
    }
*/
    public Integer getProductoId() {
        return productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
