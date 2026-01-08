package FunOnTrip.ecommerce.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA: Pedido
 *
 * Mapea la tabla "Pedidos" (plural en BD) a la clase "Pedido" (singular en Java).
 *
 * Conexiones:
 * - Se conecta con Usuario mediante la FK "Usuarios_idUsuarios" (ManyToOne).
 * - Se conecta con DetallePedido mediante OneToMany (un pedido tiene muchos detalles).
 *
 * Campos monetarios:
 * - subtotal, impuestos y total usan DECIMAL(10,2) en BD, por eso aquí usamos BigDecimal.
 */
@Entity
@Table(name = "Pedidos")
public class Pedido {

    /**
     * PK de la tabla Pedidos: idPedidos (AUTO_INCREMENT).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPedidos")
    private Integer id;

    /**
     * Relación ManyToOne con Usuario.
     * Aquí se conecta con la tabla Usuarios usando la FK "Usuarios_idUsuarios".
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Usuarios_idUsuarios", nullable = false)
    private Usuario usuario;

    /**
     * Columna FK expuesta solo para lectura (útil para responder JSON sin cargar Usuario).
     * No se inserta/actualiza directamente porque la FK la maneja la relación "usuario".
     */
    @Column(name = "Usuarios_idUsuarios", insertable = false, updatable = false)
    private Integer usuarioId;

    /**
     * Relación OneToMany con DetallePedido.
     * mappedBy = "pedido" indica que la FK vive en Detalle_pedidos.Pedidos_idPedidos.
     *
     * cascade = ALL y orphanRemoval = true permiten persistir/borrar detalles junto con el pedido.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "impuestos", precision = 10, scale = 2, nullable = false)
    private BigDecimal impuestos;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    /**
     * BD usa ENUM('pendiente','pagado','procesando','enviado','completado','cancelado')
     * Por eso el enum está en minúsculas: así @Enumerated(EnumType.STRING) guarda el mismo valor en BD.
     */
    public enum Estado {
        pendiente,
        pagado,
        procesando,
        enviado,
        completado,
        cancelado
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.pendiente;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    /**
     * En BD: fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP.
     * Se deja insertable/updatable en false para que lo maneje la BD.
     */
    @Column(name = "fecha_pedido", insertable = false, updatable = false)
    private LocalDateTime fechaPedido;

    /**
     * En BD: fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP.
     * Se deja insertable/updatable en false para que lo maneje la BD.
     */
    @Column(name = "fecha_actualizacion", insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;

    public Pedido() {
    }

    /* =========================
       Helpers de relación
       ========================= */

    /**
     * Agrega un detalle al pedido y asegura la conexión bidireccional:
     * - detalle.setPedido(this)
     * Esto es clave para que JPA guarde correctamente la FK Pedidos_idPedidos en Detalle_pedidos.
     */
    public void addDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
        detalle.setPedido(this);
    }

    /**
     * Elimina un detalle y asegura la desconexión bidireccional.
     */
    public void removeDetalle(DetallePedido detalle) {
        this.detalles.remove(detalle);
        detalle.setPedido(null);
    }

    /* =========================
       Getters / Setters
       ========================= */

    public Integer getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}
