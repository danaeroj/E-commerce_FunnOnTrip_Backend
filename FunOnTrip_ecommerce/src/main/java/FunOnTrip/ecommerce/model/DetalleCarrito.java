package FunOnTrip.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalle_carrito")
public class DetalleCarrito {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDetalleCarrito;
	
	private Integer cantidad;
	
	private BigDecimal subtotal;
	
	private LocalDateTime fechaAgregado;
	
	// Relación: Muchos detalles pertenecen a un Carrito
	@ManyToOne
	@JoinColumn(name = "Carrito_idCarrito", nullable = false)
	private Carrito carrito;
	
	// Relación: Muchos detalles pueden tener el mismo Producto
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
		this.calcularSubtotal();
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
		this.calcularSubtotal();
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
		this.calcularSubtotal();
	}
	
	// Método para calcular el subtotal automáticamente
	private void calcularSubtotal() {
		if (this.producto != null && this.cantidad != null) {
			this.subtotal = BigDecimal.valueOf(this.producto.getPrecio())
					.multiply(BigDecimal.valueOf(this.cantidad));
		}
	}
	
	@Override
	public String toString() {
		return "DetalleCarrito [idDetalleCarrito=" + idDetalleCarrito + ", cantidad=" + cantidad + ", subtotal="
				+ subtotal + ", fechaAgregado=" + fechaAgregado + "]";
	}
}