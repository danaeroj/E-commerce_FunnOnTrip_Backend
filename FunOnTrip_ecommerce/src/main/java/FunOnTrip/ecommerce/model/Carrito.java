package FunOnTrip.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "Carrito")
public class Carrito {

    // ENUM DENTRO DE LA CLASE
    public enum EstadoCarrito {
        ACTIVO,       // EN MAYÚSCULAS
        PENDIENTE,    // EN MAYÚSCULAS  
        COMPLETADO,   // EN MAYÚSCULAS
        CANCELADO;    // EN MAYÚSCULAS
        
        // MÉTODO PARA CONVERTIR STRING A ENUM (ignora mayúsculas/minúsculas)
        public static EstadoCarrito fromString(String text) {
            if (text == null) return null;
            for (EstadoCarrito estado : EstadoCarrito.values()) {
                if (estado.name().equalsIgnoreCase(text)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado de carrito no válido: " + text + 
                ". Valores válidos: ACTIVO, PENDIENTE, COMPLETADO, CANCELADO");
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrito;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarrito estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "Usuarios_idUsuarios")
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCarrito> detalles = new ArrayList<>();

    // Constructores
    public Carrito() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoCarrito.ACTIVO;
    }

    public Carrito(Usuario usuario) {
        this();
        this.usuario = usuario;
    }

    // AGREGA ESTE MÉTODO PARA @PrePersist
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoCarrito.ACTIVO;
        }
    }

    // Actualizar fecha antes de cada update
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}