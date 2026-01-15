package FunOnTrip.ecommerce.service;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.DetalleCarrito;
import FunOnTrip.ecommerce.model.EstadoCarrito;
import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.CarritoRepository;
import FunOnTrip.ecommerce.repository.DetalleCarritoRepository;
import FunOnTrip.ecommerce.repository.ProductoRepository;
import FunOnTrip.ecommerce.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final DetalleCarritoRepository detalleCarritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          DetalleCarritoRepository detalleCarritoRepository,
                          UsuarioRepository usuarioRepository,
                          ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.detalleCarritoRepository = detalleCarritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    /* ==========================
       CARRITO ACTIVO
       ========================== */

    @Transactional
    public Carrito getCarritoActivo(Integer usuarioId) {
        return carritoRepository
                .findByUsuario_IdAndEstado(usuarioId.longValue(), EstadoCarrito.ACTIVO)
                .orElseGet(() -> createCarrito(usuarioId));
    }

    @Transactional
    public Carrito createCarrito(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId.longValue())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no existe"));

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setEstado(EstadoCarrito.ACTIVO);

        return carritoRepository.save(carrito);
    }

    /* ==========================
       DETALLES DEL CARRITO
       ========================== */

    @Transactional(readOnly = true)
    public List<DetalleCarrito> getDetalles(Integer usuarioId) {
        Carrito carrito = getCarritoActivo(usuarioId);
        return detalleCarritoRepository.findByCarrito_Id(carrito.getId());
    }

    @Transactional
    public DetalleCarrito addProducto(Integer usuarioId, Long productoId, Integer cantidad) {

        if (productoId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productoId requerido");

        if (cantidad == null || cantidad <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cantidad inválida");

        Carrito carrito = getCarritoActivo(usuarioId);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no existe"));

        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), productoId)
                .orElseGet(() -> {
                    DetalleCarrito nuevo = new DetalleCarrito();
                    nuevo.setCarrito(carrito);
                    nuevo.setProducto(producto);
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        detalle.setCantidad(detalle.getCantidad() + cantidad);
        return detalleCarritoRepository.save(detalle);
    }

    @Transactional
    public DetalleCarrito updateCantidad(Integer usuarioId, Long productoId, Integer cantidad) {

        Carrito carrito = getCarritoActivo(usuarioId);

        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), productoId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no está en el carrito"));

        if (cantidad == null || cantidad <= 0) {
            detalleCarritoRepository.delete(detalle);
            return null;
        }

        detalle.setCantidad(cantidad);
        return detalleCarritoRepository.save(detalle);
    }

    @Transactional
    public void removeProducto(Integer usuarioId, Long productoId) {

        Carrito carrito = getCarritoActivo(usuarioId);

        DetalleCarrito detalle = DetalleCarritoRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), productoId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no está en el carrito"));

        detalleCarritoRepository.delete(detalle);
    }
}

