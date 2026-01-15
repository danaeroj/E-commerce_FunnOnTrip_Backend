package FunOnTrip.ecommerce.service;

import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.repository.ProductoRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;

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
   
    @Transactional
    public Producto actualizarStock(Long id, Integer stock) {
      if (stock == null || stock < 0) throw new ResponseStatusException(BAD_REQUEST, "stock inválido");
      Producto p = obtenerProductoPorId(id);
      p.setStock(stock);
      return repository.save(p);
    }

    @Transactional
    public Producto actualizarPrecio(Long id, java.math.BigDecimal precio) {
      if (precio == null || precio.compareTo(java.math.BigDecimal.ZERO) < 0)
        throw new ResponseStatusException(BAD_REQUEST, "precio inválido");
      Producto p = obtenerProductoPorId(id);
      // si tu campo es Double, convierte:
      p.setPrecio(precio.doubleValue());
      return repository.save(p);
    }
    
 // DELETE lógico
    public void eliminarProducto(Long id) {
        Producto producto = obtenerProductoPorId(id);
        producto.setActivo(false);
        repository.save(producto);
    }

	public Producto updateProducto(Long id, Producto producto) {
		// TODO Auto-generated method stub
		return null;
	}

}

