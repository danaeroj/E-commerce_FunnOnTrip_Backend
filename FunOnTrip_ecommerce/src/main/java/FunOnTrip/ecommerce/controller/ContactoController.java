package FunOnTrip.ecommerce.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import FunOnTrip.ecommerce.model.Contacto;
import FunOnTrip.ecommerce.service.ContactoService;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @PostMapping
    public ResponseEntity<Contacto> crear(@Valid @RequestBody Contacto contacto) {
        return ResponseEntity.ok(contactoService.crear(contacto));
    }

    @GetMapping
    public List<Contacto> listar() {
        return contactoService.obtenerTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        contactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
