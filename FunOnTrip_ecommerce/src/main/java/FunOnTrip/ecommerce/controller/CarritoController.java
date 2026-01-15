package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "*")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<List<Carrito>> getAll() {
        return ResponseEntity.ok(carritoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(carritoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> getCarritoUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.getOrCreateCarritoActivo(usuarioId));
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> create(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.crearCarrito(usuarioId));
    }

    // VERSIÓN CON QUERY PARAM
    @PutMapping("/{id}/estado")
    public ResponseEntity<Carrito> updateEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {
        
        // Usa el método fromString del ENUM interno
        Carrito.EstadoCarrito estadoEnum = Carrito.EstadoCarrito.fromString(estado);
        return ResponseEntity.ok(carritoService.updateEstadoCarrito(id, estadoEnum));
    }
    
    // VERSIÓN CON JSON EN BODY (opcional)
    @PutMapping("/{id}")
    public ResponseEntity<Carrito> updateCarrito(
            @PathVariable Integer id,
            @RequestBody UpdateEstadoRequest request) {
        
        Carrito.EstadoCarrito estadoEnum = Carrito.EstadoCarrito.fromString(request.getEstado());
        return ResponseEntity.ok(carritoService.updateEstadoCarrito(id, estadoEnum));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        carritoService.eliminarCarrito(id);
        return ResponseEntity.noContent().build();
    }
    
    // CLASE INTERNA PARA EL REQUEST
    public static class UpdateEstadoRequest {
        private String estado;
        
        public String getEstado() {
            return estado;
        }
        
        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}