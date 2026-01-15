package FunOnTrip.ecommerce.service;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.CarritoItem;
import FunOnTrip.ecommerce.model.EstadoCarrito;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.CarritoItemRepository;
import FunOnTrip.ecommerce.repository.CarritoRepository;
import FunOnTrip.ecommerce.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class CarritoService {

  private final CarritoRepository carritoRepository;
  private final CarritoItemRepository carritoItemRepository;
  private final UsuarioRepository usuarioRepository;

  public CarritoService(CarritoRepository carritoRepository,
                        CarritoItemRepository carritoItemRepository,
                        UsuarioRepository usuarioRepository) {
    this.carritoRepository = carritoRepository;
    this.carritoItemRepository = carritoItemRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @Transactional(readOnly = true)
  public Carrito getActivo(Long usuarioId) {
    return carritoRepository.findByUsuario_IdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
      .orElseGet(() -> createCarrito(usuarioId));
  }

  @Transactional
  public Carrito createCarrito(Long usuarioId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no existe"));

    Carrito c = new Carrito(usuario);
    c.setEstado(EstadoCarrito.ACTIVO);
    return carritoRepository.save(c);
  }

  @Transactional(readOnly = true)
  public List<CarritoItem> getItems(Long usuarioId) {
    Carrito c = getActivo(usuarioId);
    return carritoItemRepository.findByCarrito_Id(c.getId());
  }

  @Transactional
  public void addItem(Long usuarioId, Long productoId, Integer cantidad) {
    if (productoId == null) throw new ResponseStatusException(BAD_REQUEST, "productoId requerido");
    if (cantidad == null || cantidad <= 0) throw new ResponseStatusException(BAD_REQUEST, "cantidad inválida");

    Carrito c = getActivo(usuarioId);

    CarritoItem item = carritoItemRepository
      .findByCarrito_IdAndProductoId(c.getId(), productoId)
      .orElseGet(() -> {
        CarritoItem ci = new CarritoItem();
        ci.setCarrito(c);
        ci.setProductoId(productoId);
        ci.setCantidad(0);
        return ci;
      });

    item.setCantidad(item.getCantidad() + cantidad);
    carritoItemRepository.save(item);
  }

  @Transactional
  public void updateCantidad(Long usuarioId, Long productoId, Integer cantidad) {
    Carrito c = getActivo(usuarioId);

    CarritoItem item = carritoItemRepository
      .findByCarrito_IdAndProductoId(c.getId(), productoId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Item no existe"));

    if (cantidad == null || cantidad <= 0) {
      carritoItemRepository.delete(item); // si baja a 0, se elimina
    } else {
      item.setCantidad(cantidad);
      carritoItemRepository.save(item);
    }
  }

  @Transactional
  public void removeItem(Long usuarioId, Long productoId) {
    Carrito c = getActivo(usuarioId);

    CarritoItem item = carritoItemRepository
      .findByCarrito_IdAndProductoId(c.getId(), productoId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Item no existe"));

    carritoItemRepository.delete(item);
  }
}
