package FunOnTrip.ecommerce.controller;

import java.util.List;

import FunOnTrip.ecommerce.model.DetalleCarrito;
import FunOnTrip.ecommerce.service.DetalleCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/detalles-carrito")
public class DetalleCarritoController {

	private final DetalleCarritoService detalleService;

	@Autowired
	public DetalleCarritoController(DetalleCarritoService detalleService) {
		this.detalleService = detalleService;
	}

	/**
	 * GET /api/detalles-carrito/carrito/{carritoId}
	 * Obtener todos los items de un carrito
	 */
	@GetMapping("/carrito/{carritoId}")
	public List<DetalleCarrito> getDetallesByCarrito(@PathVariable Integer carritoId) {
		return detalleService.getDetallesByCarrito(carritoId);
	}

	/**
	 * GET /api/detalles-carrito/{id}
	 * Obtener un detalle por ID
	 */
	@GetMapping("/{id}")
	public DetalleCarrito getDetalleById(@PathVariable Integer id) {
		return detalleService.getDetalleById(id);
	}

	/**
	 * POST /api/detalles-carrito?carritoId=1&productoId=2&cantidad=3
	 * Agregar producto al carrito
	 */
	@PostMapping
	public DetalleCarrito addProducto(
			@RequestParam Integer carritoId,
			@RequestParam Long productoId,
			@RequestParam Integer cantidad) {
		return detalleService.addProducto(carritoId, productoId, cantidad);
	}

	/**
	 * PUT /api/detalles-carrito/{id}?cantidad=5
	 * Actualizar cantidad de un item
	 */
	@PutMapping("/{id}")
	public DetalleCarrito updateCantidad(
			@PathVariable Integer id,
			@RequestParam Integer cantidad) {
		return detalleService.updateCantidad(id, cantidad);
	}

	/**
	 * DELETE /api/detalles-carrito/{id}
	 * Eliminar un item del carrito
	 */
	@DeleteMapping("/{id}")
	public void deleteDetalle(@PathVariable Integer id) {
		detalleService.deleteDetalle(id);
	}
}

