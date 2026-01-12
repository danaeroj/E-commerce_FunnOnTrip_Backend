package FunOnTrip.ecommerce.service;

import java.util.List;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.EstadoCarrito;
import FunOnTrip.ecommerce.model.Usuario;
import FunOnTrip.ecommerce.repository.CarritoRepository;
import FunOnTrip.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoService {

	private final CarritoRepository carritoRepository;
	private final UsuarioRepository usuarioRepository;

	@Autowired
	public CarritoService(CarritoRepository carritoRepository, UsuarioRepository usuarioRepository) {
		this.carritoRepository = carritoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Obtener todos los carritos
	 */
	public List<Carrito> getAllCarritos() {
		return carritoRepository.findAll();
	}

	/**
	 * Obtener un carrito por ID
	 */
	public Carrito getCarritoById(Integer id) {
		return carritoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("El carrito con id [" + id + "] no existe"));
	}

	/**
	 * Obtener o crear carrito activo de un usuario
	 * Si el usuario no tiene carrito activo, crea uno nuevo
	 */
	@Transactional
	public Carrito getOrCreateCarritoActivo(Long usuarioId) {
		return carritoRepository.findByUsuarioIdUsuariosAndEstado(usuarioId, EstadoCarrito.ACTIVO)
				.orElseGet(() -> {
					Usuario usuario = usuarioRepository.findById(usuarioId)
							.orElseThrow(() -> new IllegalArgumentException("Usuario con id [" + usuarioId + "] no existe"));

					Carrito nuevoCarrito = new Carrito(usuario);
					return carritoRepository.save(nuevoCarrito);
				});
	}

	/**
	 * Crear un nuevo carrito para un usuario
	 */
	@Transactional
	public Carrito createCarrito(Long usuarioId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new IllegalArgumentException("Usuario con id [" + usuarioId + "] no existe"));

		Carrito carrito = new Carrito(usuario);
		return carritoRepository.save(carrito);
	}

	/**
	 * Actualizar estado del carrito
	 */
	@Transactional
	public Carrito updateEstadoCarrito(Integer carritoId, EstadoCarrito nuevoEstado) {
		Carrito carrito = getCarritoById(carritoId);
		carrito.setEstado(nuevoEstado);
		return carritoRepository.save(carrito);
	}

	/**
	 * Eliminar un carrito
	 */
	@Transactional
	public Carrito deleteCarrito(Integer id) {
		Carrito carrito = getCarritoById(id);
		carritoRepository.delete(carrito);
		return carrito;
	}
}