package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Contacto;
import FunOnTrip.ecommerce.service.ContactoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "*")
public class ContactoController {

  private final ContactoService contactoService;

  public ContactoController(ContactoService contactoService) {
    this.contactoService = contactoService;
  }

  // PUBLIC: crear mensaje (sin token)
  @PostMapping
  public ResponseEntity<Contacto> crear(@Valid @RequestBody Contacto contacto) {
    return ResponseEntity.ok(contactoService.crear(contacto));
  }

  // ADMIN: ver todos
  @GetMapping
  public List<Contacto> listar() {
    return contactoService.obtenerTodos();
  }

  // ADMIN: ver no atendidos
  @GetMapping("/no-atendidos")
  public List<Contacto> listarNoAtendidos() {
    return contactoService.obtenerNoAtendidos();
  }

  // ADMIN: marcar atendido
  @PatchMapping("/{id}/atender")
  public Contacto marcarComoAtendido(@PathVariable Integer id) {
    return contactoService.marcarComoAtendido(id);
  }
}
