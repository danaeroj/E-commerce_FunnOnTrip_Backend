package FunOnTrip.ecommerce.service;

import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    @Autowired
    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Producto crearProducto(Producto producto) {
        producto.setActivo(true);
        return repository.save(producto);
    }

    // READ
    public List<Producto> obtenerProductos() {
        return repository.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public List<Producto> obtenerProductosActivos() {
        return repository.findByActivoTrue();
    }

    // UPDATE
    public Producto actualizarProducto(Long id, Producto producto) {
        Producto existente = obtenerProductoPorId(id);

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setActivo(producto.getActivo());

        return repository.save(existente);
    }

    // DELETE lógico
    public Producto eliminarProducto(Long id) {
        Producto producto = obtenerProductoPorId(id);
        producto.setActivo(false);
        return repository.save(producto);
    }
}

