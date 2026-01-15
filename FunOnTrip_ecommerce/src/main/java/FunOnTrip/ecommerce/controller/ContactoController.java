package FunOnTrip.ecommerce.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import FunOnTrip.ecommerce.model.Contacto;
import FunOnTrip.ecommerce.service.ContactoService;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    // 🔓 PÚBLICO: Crear contacto (sin token)
    @PostMapping
    public ResponseEntity<Contacto> crear(@Valid @RequestBody Contacto contacto) {
        return ResponseEntity.ok(contactoService.crear(contacto));
    }

    // 🔐 ADMIN: Obtener todos los contactos
    @GetMapping
    public List<Contacto> listar() {
        return contactoService.obtenerTodos();
    }

    //  ADMIN: Obtener contactos no atendidos
    @GetMapping("/no-atendidos")
    public List<Contacto> listarNoAtendidos() {
        return contactoService.obtenerNoAtendidos();
    }

    //  ADMIN: Obtener contacto por ID
    @GetMapping("/{id}")
    public Contacto obtener(@PathVariable ("id") Integer id) {
        return contactoService.obtenerPorId(id);
    }

    //  ADMIN: Actualizar contacto
    @PutMapping("/{id}")
    public Contacto actualizar(@PathVariable ("id") Integer id,
                               @Valid @RequestBody Contacto contacto) {
        return contactoService.actualizar(id, contacto);
    }

    //  ADMIN: Eliminar contacto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable ("id") Integer id) {
        contactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //  ADMIN: Marcar como atendido
    @PatchMapping("/{id}/atender")
    public Contacto marcarComoAtendido(@PathVariable ("id") Integer id) {
        return contactoService.marcarComoAtendido(id);
    }
}