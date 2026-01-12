package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // para que el front no llore después
public class ProductoController {

    private final ProductoService service;

    @Autowired
    public ProductoController(ProductoService service) {
        this.service = service;
    }

    //CREATE
    // POST http://localhost:8080/api/productos
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(service.crearProducto(producto));
    }

    //READ ALL
    // GET http://localhost:8080/api/productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        return ResponseEntity.ok(service.obtenerProductos());
    }

    //READ BY ID
    // GET http://localhost:8080/api/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerProductoPorId(id));
    }

    // READ ACTIVOS
    // GET http://localhost:8080/api/productos/activos
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> obtenerProductosActivos() {
        return ResponseEntity.ok(service.obtenerProductosActivos());
    }

    // UPDATE
    // PUT http://localhost:8080/api/productos/1
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto
    ) {
        return ResponseEntity.ok(service.actualizarProducto(id, producto));
    }

    // DELETE 
    // DELETE http://localhost:8080/api/productos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Producto> eliminarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(service.eliminarProducto(id));
    }
}
