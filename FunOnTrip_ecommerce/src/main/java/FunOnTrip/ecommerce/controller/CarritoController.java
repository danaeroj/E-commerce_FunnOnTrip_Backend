package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.DetalleCarrito;
import FunOnTrip.ecommerce.service.CarritoService;
import FunOnTrip.ecommerce.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "*")
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;

    public CarritoController(CarritoService carritoService, UsuarioService usuarioService) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
    }

    /**
     * GET /api/carritos/me
     * Obtiene el carrito activo del usuario autenticado
     */
    @GetMapping("/me")
    public Carrito getMiCarrito(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        Long usuarioId = usuarioService
                .getByEmail(user.getUsername())
                .getId();

        return carritoService.getCarritoActivo(usuarioId.intValue());
    }

    /**
     * GET /api/carritos/me/items
     * Lista los productos (DetalleCarrito) del carrito activo
     */
    @GetMapping("/me/items")
    public List<DetalleCarrito> getItems(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        Long usuarioId = usuarioService
                .getByEmail(user.getUsername())
                .getId();

        return carritoService.getDetalles(usuarioId.intValue());
    }

    /**
     * POST /api/carritos/me/items
     * Agrega un producto al carrito.
     * - Si el producto ya existe → incrementa cantidad
     * - Si no existe → crea nuevo DetalleCarrito
     */
    @PostMapping("/me/items")
    public DetalleCarrito addItem(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestBody AddItemRequest request) {

        Long usuarioId = usuarioService
                .getByEmail(user.getUsername())
                .getId();

        return carritoService.addProducto(
                usuarioId.intValue(),
                request.productoId,
                request.cantidad
        );
    }

    /**
     * PATCH /api/carritos/me/items/{productoId}?cantidad=3
     * Actualiza la cantidad exacta de un producto
     */
    @PatchMapping("/me/items/{productoId}")
    public DetalleCarrito updateCantidad(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {

        Long usuarioId = usuarioService
                .getByEmail(user.getUsername())
                .getId();

        return carritoService.updateCantidad(
                usuarioId.intValue(),
                productoId,
                cantidad
        );
    }

    /**
     * DELETE /api/carritos/me/items/{productoId}
     * Elimina un producto del carrito
     */
    @DeleteMapping("/me/items/{productoId}")
    public void removeItem(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @PathVariable Long productoId) {

        Long usuarioId = usuarioService
                .getByEmail(user.getUsername())
                .getId();

        carritoService.removeProducto(
                usuarioId.intValue(),
                productoId
        );
    }

    /**
     * DTO para agregar items
     */
    public static class AddItemRequest {
        public Long productoId;
        public Integer cantidad;
    }
}


