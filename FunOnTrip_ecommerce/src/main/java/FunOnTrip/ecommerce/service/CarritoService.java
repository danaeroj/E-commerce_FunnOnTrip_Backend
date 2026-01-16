package FunOnTrip.ecommerce.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.CarritoRepository;
import FunOnTrip.ecommerce.repository.UsuarioRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoService(CarritoRepository carritoRepository, UsuarioRepository usuarioRepository) {
        this.carritoRepository = carritoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Carrito> obtenerTodos() {
        return carritoRepository.findAllWithDetalles();
    }

    // Método que SI carga los detalles - usa el método con @EntityGraph
    public Carrito obtenerPorIdConDetalles(Integer id) {
        return carritoRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new IllegalArgumentException("Carrito con id [" + id + "] no encontrado"));
    }
    

    // Método normal (puedes usar este o el de arriba, ambos cargan detalles)
    public Carrito obtenerPorId(Integer id) {
        return obtenerPorIdConDetalles(id);  // Llama al mismo método
    }
    
  
    
    @Transactional
    public Carrito getOrCreateCarritoActivo(Long usuarioId) {
        // Usa el método con @EntityGraph
        return carritoRepository.findByUsuario_IdAndEstado(usuarioId, Carrito.EstadoCarrito.ACTIVO)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Usuario con id [" + usuarioId + "] no existe"));

                    Carrito nuevoCarrito = new Carrito(usuario);
                    nuevoCarrito.setEstado(Carrito.EstadoCarrito.ACTIVO);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    @Transactional
    public Carrito crearCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario con id [" + usuarioId + "] no existe"));

        Carrito carrito = new Carrito(usuario);
        carrito.setEstado(Carrito.EstadoCarrito.ACTIVO);
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito updateEstadoCarrito(Integer carritoId, Carrito.EstadoCarrito nuevoEstado) {
        Carrito carrito = obtenerPorId(carritoId);
        carrito.setEstado(nuevoEstado);
        return carritoRepository.save(carrito);
    }

    @Transactional
    public void eliminarCarrito(Integer id) {
        Carrito carrito = obtenerPorId(id);
        carritoRepository.delete(carrito);
    }
}