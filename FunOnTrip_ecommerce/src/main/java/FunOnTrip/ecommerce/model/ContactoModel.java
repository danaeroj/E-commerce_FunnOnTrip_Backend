package FunOnTrip.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "Contacto")
public class ContactoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContacto;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank
    @Email
    @Column(nullable = false, length = 155)
    private String correoElectronico;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String asunto;

    @NotBlank
    @Column(nullable = false, length = 445)
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @PrePersist
    public void prePersist() {
        this.fechaEnvio = LocalDate.now();
    }

    // getters y setters
}
