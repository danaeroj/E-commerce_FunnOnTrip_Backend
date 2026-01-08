package FunOnTrip.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "carrito")
public class Carrito {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCarrito;
	
	@Enumerated(EnumType.STRING)
	private EstadoCarrito estado;
	
	private LocalDateTime fechaCreacion;
	
	// Relación: Muchos carritos pertenecen a un Usuario
	@ManyToOne
	@JoinColumn(name = "Usuarios_idUsuarios", nullable = false)
	private Usuario usuario;
	
	// Relación: Un carrito tiene muchos detalles
	@OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleCarrito> detalles = new ArrayList<>();
	
	// Constructores
	public Carrito() {
		this.fechaCreacion = LocalDateTime.now();
		this.estado = EstadoCarrito.ACTIVO;
	}
	
	public Carrito(Usuario usuario) {
		this();
		this.usuario = usuario;
	}
	
	// Getters y Setters
	public Integer getIdCarrito() {
		return idCarrito;
	}
	
	public void setIdCarrito(Integer idCarrito) {
		this.idCarrito = idCarrito;
	}
	
	public EstadoCarrito getEstado() {
		return estado;
	}
	
	public void setEstado(EstadoCarrito estado) {
		this.estado = estado;
	}
	
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}
	
	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public List<DetalleCarrito> getDetalles() {
		return detalles;
	}
	
	public void setDetalles(List<DetalleCarrito> detalles) {
		this.detalles = detalles;
	}
	
	// Métodos auxiliares para manejar la relación bidireccional
	public void addDetalle(DetalleCarrito detalle) {
		detalles.add(detalle);
		detalle.setCarrito(this);
	}
	
	public void removeDetalle(DetalleCarrito detalle) {
		detalles.remove(detalle);
		detalle.setCarrito(null);
	}
	
	@Override
	public String toString() {
		return "Carrito [idCarrito=" + idCarrito + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + "]";
	}
}
