package FunOnTrip.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "carrito_items",
       uniqueConstraints = @UniqueConstraint(columnNames = {"carrito_id", "producto_id"}))
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad = 1; // Valor por defecto

    // Constructores
    public CarritoItem() {}

    public CarritoItem(Carrito carrito, Long productoId, Integer cantidad) {
        this.carrito = carrito;
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    // Método útil para incrementar cantidad
    public void incrementarCantidad(Integer cantidad) {
        this.cantidad += cantidad;
    }

    // equals() y hashCode() para comparaciones
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarritoItem)) return false;
        CarritoItem that = (CarritoItem) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // toString() para depuración
    @Override
    public String toString() {
        return "CarritoItem{" +
                "id=" + id +
                ", carritoId=" + (carrito != null ? carrito.getId() : null) +
                ", productoId=" + productoId +
                ", cantidad=" + cantidad +
                '}';
    }
}