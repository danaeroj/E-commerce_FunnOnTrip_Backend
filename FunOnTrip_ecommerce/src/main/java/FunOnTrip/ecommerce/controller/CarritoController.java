package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Carrito;
import FunOnTrip.ecommerce.model.CarritoItem;
import FunOnTrip.ecommerce.service.CarritoService;
import FunOnTrip.ecommerce.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins="*")
public class CarritoController {

  private final CarritoService carritoService;
  private final UsuarioService usuarioService;

  public CarritoController(CarritoService carritoService, UsuarioService usuarioService) {
    this.carritoService = carritoService;
    this.usuarioService = usuarioService;
  }

  // GET /api/carritos/me -> carrito activo
  @GetMapping("/me")
  public Carrito getMiCarrito(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();
    return carritoService.getActivo(usuarioId);
  }

  // GET /api/carritos/me/items -> items
  @GetMapping("/me/items")
  public List<CarritoItem> getItems(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();
    return carritoService.getItems(usuarioId);
  }

  // POST /api/carritos/me/items -> incrementa
  @PostMapping("/me/items")
  public void addItem(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                      @RequestBody AddItemRequest req) {
    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();
    carritoService.addItem(usuarioId, req.productoId, req.cantidad);
  }

  // PATCH /api/carritos/me/items/{productoId}?cantidad=3 -> set cantidad exacta
  @PatchMapping("/me/items/{productoId}")
  public void updateQty(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                        @PathVariable Long productoId,
                        @RequestParam Integer cantidad) {
    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();
    carritoService.updateCantidad(usuarioId, productoId, cantidad);
  }

  // DELETE /api/carritos/me/items/{productoId}
  @DeleteMapping("/me/items/{productoId}")
  public void removeItem(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                         @PathVariable Long productoId) {
    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();
    carritoService.removeItem(usuarioId, productoId);
  }

  public static class AddItemRequest {
    public Long productoId;
    public Integer cantidad;
  }
}
