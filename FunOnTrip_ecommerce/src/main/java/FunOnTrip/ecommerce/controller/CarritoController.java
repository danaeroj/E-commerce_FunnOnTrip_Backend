package FunOnTrip.ecommerce.controller;

import java.util.List;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.EstadoCarrito;
import FunOnTrip.ecommerce.service.CarritoService;
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
@RequestMapping("/api/carritos")
public class CarritoController {

	private final CarritoService carritoService;

	@Autowired
	public CarritoController(CarritoService carritoService) {
		this.carritoService = carritoService;
	}

	/**
	 * GET /api/carritos
	 * Obtener todos los carritos
	 */
	@GetMapping
	public List<Carrito> getAllCarritos() {
		return carritoService.getAllCarritos();
	}

	/**
	 * GET /api/carritos/{id}
	 * Obtener un carrito por ID
	 */
	@GetMapping("/{id}")
	public Carrito getCarritoById(@PathVariable Integer id) {
		return carritoService.getCarritoById(id);
	}

	/**
	 * GET /api/carritos/usuario/{usuarioId}
	 * Obtener o crear carrito activo de un usuario
	 */
	@GetMapping("/usuario/{usuarioId}")
	public Carrito getCarritoActivo(@PathVariable Long usuarioId) {
		return carritoService.getOrCreateCarritoActivo(usuarioId);
	}

	/**
	 * POST /api/carritos/usuario/{usuarioId}
	 * Crear un nuevo carrito para un usuario
	 */
	@PostMapping("/usuario/{usuarioId}")
	public Carrito createCarrito(@PathVariable Long usuarioId) {
		return carritoService.createCarrito(usuarioId);
	}

	/**
	 * PUT /api/carritos/{id}/estado?estado=CONVERTIDO
	 * Actualizar estado del carrito
	 */
	@PutMapping("/{id}/estado")
	public Carrito updateEstado(@PathVariable Integer id, @RequestParam EstadoCarrito estado) {
		return carritoService.updateEstadoCarrito(id, estado);
	}

	/**
	 * DELETE /api/carritos/{id}
	 * Eliminar un carrito
	 */
	@DeleteMapping("/{id}")
	public Carrito deleteCarrito(@PathVariable Integer id) {
		return carritoService.deleteCarrito(id);
	}
}
