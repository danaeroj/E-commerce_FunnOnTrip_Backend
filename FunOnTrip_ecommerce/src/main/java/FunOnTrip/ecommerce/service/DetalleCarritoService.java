package FunOnTrip.ecommerce.service;

import java.util.List;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.DetalleCarrito;
import FunOnTrip.ecommerce.model.Producto;
import FunOnTrip.ecommerce.repository.CarritoRepository;
import FunOnTrip.ecommerce.repository.DetalleCarritoRepository;
import FunOnTrip.ecommerce.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetalleCarritoService {

	private final DetalleCarritoRepository detalleRepository;
	private final CarritoRepository carritoRepository;
	private final ProductoRepository productoRepository;

	@Autowired
	public DetalleCarritoService(DetalleCarritoRepository detalleRepository,
			CarritoRepository carritoRepository,
			ProductoRepository productoRepository) {
		this.detalleRepository = detalleRepository;
		this.carritoRepository = carritoRepository;
		this.productoRepository = productoRepository;
	}

	/**
	 * Obtener todos los detalles de un carrito
	 */
	public List<DetalleCarrito> getDetallesByCarrito(Integer carritoId) {
		return detalleRepository.findByCarritoIdCarrito(carritoId);
	}

	/**
	 * Obtener un detalle por ID
	 */
	public DetalleCarrito getDetalleById(Integer id) {
		return detalleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Detalle con id [" + id + "] no existe"));
	}

	/**
	 * Agregar producto al carrito
	 * Si el producto ya existe, actualiza la cantidad
	 */
	@Transactional
	public DetalleCarrito addProducto(Integer carritoId, Long productoId, Integer cantidad) {
		Carrito carrito = carritoRepository.findById(carritoId)
				.orElseThrow(() -> new IllegalArgumentException("Carrito con id [" + carritoId + "] no existe"));

		Producto producto = productoRepository.findById(productoId)
				.orElseThrow(() -> new IllegalArgumentException("Producto con id [" + productoId + "] no existe"));

		
		
		// Verificar si el producto ya existe en el carrito
		return detalleRepository.findByCarritoIdCarritoAndProductoIdProducto(carritoId, productoId)
				.map(detalle -> {
					// Si ya existe, sumar la cantidad
					detalle.setCantidad(detalle.getCantidad() + cantidad);
					return detalleRepository.save(detalle);
				})
				.orElseGet(() -> {
					// Si no existe, crear nuevo detalle
					DetalleCarrito nuevoDetalle = new DetalleCarrito(carrito, producto, cantidad);
					return detalleRepository.save(nuevoDetalle);
				});
	}

	/**
	 * Actualizar cantidad de un detalle
	 * Si la cantidad es 0 o menor, elimina el detalle
	 */
	@Transactional
	public DetalleCarrito updateCantidad(Integer detalleId, Integer cantidad) {
		DetalleCarrito detalle = getDetalleById(detalleId);

		if (cantidad <= 0) {
			detalleRepository.delete(detalle);
			return null;
		}

		detalle.setCantidad(cantidad);
		return detalleRepository.save(detalle);
	}

	/**
	 * Eliminar un detalle del carrito
	 */
	@Transactional
	public void deleteDetalle(Integer id) {
		if (!detalleRepository.existsById(id)) {
			throw new IllegalArgumentException("Detalle con id [" + id + "] no existe");
		}
		detalleRepository.deleteById(id);
	}
}

