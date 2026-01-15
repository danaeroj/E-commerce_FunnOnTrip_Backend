package FunOnTrip.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "Detalle_carrito",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"Carrito_idCarrito", "Producto_idProducto"})
    }
)
public class DetalleCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalle_carrito")
    private Integer idDetalleCarrito;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "fecha_agregado", nullable = false, updatable = false)
    private LocalDateTime fechaAgregado;

    @ManyToOne
    @JoinColumn(name = "Carrito_idCarrito", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "Producto_idProducto", nullable = false)
    private Producto producto;

    public DetalleCarrito() {
        this.fechaAgregado = LocalDateTime.now();
    }

    public DetalleCarrito(Carrito carrito, Producto producto, Integer cantidad) {
        this();
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    @PrePersist
    @PreUpdate
    private void calcularSubtotal() {
        if (producto != null && cantidad != null) {
            this.subtotal = BigDecimal
                    .valueOf(producto.getPrecio())
                    .multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public Integer getIdDetalleCarrito() { return idDetalleCarrito; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getSubtotal() { return subtotal; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCarrito(Carrito carrito) { this.carrito = carrito; }
}

