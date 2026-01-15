package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> getAll() {
        return pedidoService.getAll();
    }

    @GetMapping("/{id}")
    public Pedido getById(@PathVariable("id") Integer id) {
        return pedidoService.getByIdWithDetalles(id);
    }


    // FIX: PathVariable + llamas al service (no al repo)
    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> getByUsuario(@PathVariable ("usuarioId") Integer usuarioId) {
        return pedidoService.getByUsuario(usuarioId);
    }


    @PostMapping
    public ResponseEntity<CreatePedidoResponse> create(@RequestBody CreatePedidoRequest request) {
        Pedido creado = pedidoService.crearPedido(request.usuarioId, request.metodoPago, request.items);

        CreatePedidoResponse resp = new CreatePedidoResponse();
        // OJO: tu entidad Pedido debe tener getId() o getIdPedidos() consistente.
        // Si tu campo es "id" con @Column(name="idPedidos"), normalmente el getter es getId().
        resp.pedidoId = creado.getId(); // <-- cambia a getIdPedidos() solo si así se llama de verdad
        resp.estado = creado.getEstado();
        resp.metodoPago = creado.getMetodoPago();
        resp.total = creado.getTotal();

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

  
    
 // PATCH - cambiar SOLO estado
 // Ej: PATCH /api/pedidos/10/estado
 // Body: {"estado":"cancelado"}
 @PatchMapping("/{id}/estado")
 public Pedido actualizarEstado(@PathVariable("id") Integer id,
                                @RequestBody UpdateEstadoRequest body) {
     if (body == null || body.estado == null) {
         throw new org.springframework.web.server.ResponseStatusException(
                 org.springframework.http.HttpStatus.BAD_REQUEST,
                 "estado es requerido"
         );
     }
     return pedidoService.actualizarEstado(id, body.estado);
 }

 public static class UpdateEstadoRequest {
     public Pedido.Estado estado;
 }



//PATCH - cambiar SOLO método de pago
//Ej: PATCH /api/pedidos/10/metodo-pago?metodoPago=TARJETA
@PatchMapping("/{id}/metodo-pago")
public Pedido updateMetodoPago(@PathVariable("id") Integer id,
                             @RequestParam("metodoPago") String metodoPago) {
  return pedidoService.actualizarMetodoPago(id, metodoPago);
}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }


    // ========= DTOs internos =========

    public static class CreatePedidoRequest {
        public Integer usuarioId;
        public String metodoPago;
        public List<PedidoService.ItemPedido> items;
        public CreatePedidoRequest() {}
    }

    public static class CreatePedidoResponse {
        public Integer pedidoId;
        public Pedido.Estado estado;
        public String metodoPago;
        public BigDecimal total;
    }
}
