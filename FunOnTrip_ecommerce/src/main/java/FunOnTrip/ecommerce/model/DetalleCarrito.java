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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Detalle_carrito")
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

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "Carrito_idCarrito", nullable = false)
	private Carrito carrito;


	@ManyToOne
	@JoinColumn(name = "Producto_idProducto", nullable = false)
	private Producto producto;

	// Constructores
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

	// Calcular subtotal automáticamente antes de guardar o actualizar
	@PrePersist
	@PreUpdate
	public void calcularSubtotal() {
		if (this.producto != null && this.cantidad != null) {
			this.subtotal = BigDecimal.valueOf(this.producto.getPrecio())
			        .multiply(BigDecimal.valueOf(this.cantidad));

		}
	}

	// Getters y Setters
	public Integer getIdDetalleCarrito() {
		return idDetalleCarrito;
	}

	public void setIdDetalleCarrito(Integer idDetalleCarrito) {
		this.idDetalleCarrito = idDetalleCarrito;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
		calcularSubtotal();
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public LocalDateTime getFechaAgregado() {
		return fechaAgregado;
	}

	public void setFechaAgregado(LocalDateTime fechaAgregado) {
		this.fechaAgregado = fechaAgregado;
	}

	public Carrito getCarrito() {
		return carrito;
	}

	public void setCarrito(Carrito carrito) {
		this.carrito = carrito;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
		calcularSubtotal();
	}

	@Override
	public String toString() {
		return "DetalleCarrito [idDetalleCarrito=" + idDetalleCarrito + ", cantidad=" + cantidad + ", subtotal="
				+ subtotal + "]";
	}
}
