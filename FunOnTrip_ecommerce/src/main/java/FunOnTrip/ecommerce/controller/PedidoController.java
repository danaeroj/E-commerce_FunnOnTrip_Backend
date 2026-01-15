package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins="*")
public class PedidoController {

  private final PedidoService pedidoService;
  private final UsuarioService usuarioService;

  public PedidoController(PedidoService pedidoService, UsuarioService usuarioService) {
    this.pedidoService = pedidoService;
    this.usuarioService = usuarioService;
  }

  // ADMIN: ver todos
  @GetMapping
  public List<Pedido> getAll() {
    return pedidoService.getAll();
  }

  // USER: mis pedidos
  @GetMapping("/mis")
  public List<Pedido> misPedidos(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();
    return pedidoService.getByUsuario(usuarioId.intValue());
  }

  // USER: crear pedido (usa usuario logeado, no usuarioId del body)
  @PostMapping
  public ResponseEntity<CreatePedidoResponse> create(@RequestBody CreatePedidoRequest request,
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();

    Pedido creado = pedidoService.crearPedido(usuarioId.intValue(), request.metodoPago, request.items);

    CreatePedidoResponse resp = new CreatePedidoResponse();
    resp.pedidoId = creado.getId();
    resp.estado = creado.getEstado();
    resp.metodoPago = creado.getMetodoPago();
    resp.total = creado.getTotal();

    return ResponseEntity.status(HttpStatus.CREATED).body(resp);
  }

  // USER: solicitar cancelación
  @PatchMapping("/{id}/solicitar-cancelacion")
  public Pedido solicitarCancelacion(@PathVariable Integer id,
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

    Long usuarioId = usuarioService.getByEmail(user.getUsername()).getId();
    return pedidoService.solicitarCancelacion(id, usuarioId.intValue());
  }

  // ADMIN: estado final
  @PatchMapping("/{id}/estado-final")
  public Pedido estadoFinal(@PathVariable Integer id, @RequestBody UpdateEstadoRequest body) {
    if (body == null || body.estado == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "estado requerido");
    return pedidoService.actualizarEstado(id, body.estado);
  }

  public static class UpdateEstadoRequest { public Pedido.Estado estado; }

  public static class CreatePedidoRequest {
    public String metodoPago;
    public List<PedidoService.ItemPedido> items;
  }

  public static class CreatePedidoResponse {
    public Integer pedidoId;
    public Pedido.Estado estado;
    public String metodoPago;
    public BigDecimal total;
  }
}

