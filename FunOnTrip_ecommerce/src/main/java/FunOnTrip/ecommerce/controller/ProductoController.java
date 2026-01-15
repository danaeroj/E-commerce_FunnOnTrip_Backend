package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // POST /api/productos (ADMIN)
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(service.crearProducto(producto));
    }

    // GET /api/productos (PUBLIC)
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        return ResponseEntity.ok(service.obtenerProductos());
    }

    // GET /api/productos/activos (PUBLIC)
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> obtenerProductosActivos() {
        return ResponseEntity.ok(service.obtenerProductosActivos());
    }

    // GET /api/productos/{id} (PUBLIC)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerProductoPorId(id));
    }

    // PATCH /api/productos/{id} (ADMIN) - update parcial
    @PatchMapping("/{id}")
    public ResponseEntity<Producto> patchProducto(@PathVariable Long id,
                                                  @RequestBody Producto producto) {
        return ResponseEntity.ok(service.updateProducto(id, producto));
    }

    // PATCH /api/productos/{id}/stock (ADMIN)
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Producto> patchStock(@PathVariable Long id,
                                               @RequestBody UpdateStockRequest body) {
        return ResponseEntity.ok(service.actualizarStock(id, body.stock));
    }

    // PATCH /api/productos/{id}/precio (ADMIN)
    @PatchMapping("/{id}/precio")
    public ResponseEntity<Producto> patchPrecio(@PathVariable Long id,
                                                @RequestBody UpdatePrecioRequest body) {
        return ResponseEntity.ok(service.actualizarPrecio(id, body.precio));
    }

    // DELETE lógico /api/productos/{id} (ADMIN)
    // REST correcto: 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // ===== DTOs =====
    public static class UpdateStockRequest {
        public Integer stock;
    }

    public static class UpdatePrecioRequest {
        public BigDecimal precio;
    }
}

 
