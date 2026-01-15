package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto creado = service.crearProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        return ResponseEntity.ok(service.obtenerProductos());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable  ("id")  Long id) {
        return ResponseEntity.ok(service.obtenerProductoPorId(id));
    }

    // READ ACTIVOS
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> obtenerProductosActivos() {
        return ResponseEntity.ok(service.obtenerProductosActivos());
    }
    @PatchMapping("/{id}")
    public Producto actualizarProducto(@PathVariable("id") Long id,
                                       @RequestBody Producto producto) {
        return service.updateProducto(id, producto);
    }

 // PATCH - cambiar SOLO stock
 // PATCH http://localhost:8080/api/productos/1/stock
 // Body: { "stock": 25 }
 @PatchMapping("/{id}/stock")
 public ResponseEntity<Producto> actualizarStock(@PathVariable ("id") Long id,
                                                @RequestBody UpdateStockRequest body) {
     return ResponseEntity.ok(service.actualizarStock(id, body.stock));
 }

 public static class UpdateStockRequest {
     public Integer stock;
 }

 // PATCH - cambiar SOLO precio
 // PATCH http://localhost:8080/api/productos/1/precio
 // Body: { "precio": 199.99 }
 @PatchMapping("/{id}/precio")
 public ResponseEntity<Producto> actualizarPrecio(@PathVariable  ("id")  Long id,
                                                 @RequestBody UpdatePrecioRequest body) {
     return ResponseEntity.ok(service.actualizarPrecio(id, body.precio));
 }

 public static class UpdatePrecioRequest {
     public Double precio; // o BigDecimal si tu modelo usa BigDecimal
 }


    // DELETE lógico (desactivar)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable  ("id")  Long id) {
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
