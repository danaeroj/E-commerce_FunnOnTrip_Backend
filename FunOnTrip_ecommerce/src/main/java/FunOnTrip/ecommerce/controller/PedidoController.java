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
    public Pedido getById(@PathVariable Integer id) {
        return pedidoService.getByIdWithDetalles(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> getByUsuario(@PathVariable Integer usuarioId) {
        return pedidoService.getByUsuario(usuarioId);
    }

    /**
     * POST /api/pedidos
     * Body ejemplo:
     * {
     *   "usuarioId": 1,
     *   "metodoPago": "cotizacion",
     *   "items": [
     *     { "productoId": 1, "cantidad": 2 },
     *     { "productoId": 2, "cantidad": 1 }
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<CreatePedidoResponse> create(@RequestBody CreatePedidoRequest request) {
        Pedido creado = pedidoService.crearPedido(request.usuarioId, request.metodoPago, request.items);

        CreatePedidoResponse resp = new CreatePedidoResponse();
        resp.pedidoId = creado.getIdPedidos();   // o creado.getId()
        resp.estado = creado.getEstado();
        resp.metodoPago = creado.getMetodoPago();
        resp.total = creado.getTotal();

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{id}/estado")
    public Pedido updateEstado(@PathVariable Integer id, @RequestParam Pedido.Estado estado) {
        return pedidoService.actualizarEstado(id, estado);
    }

    @PutMapping("/{id}/metodo-pago")
    public Pedido updateMetodoPago(@PathVariable Integer id, @RequestParam String metodoPago) {
        return pedidoService.actualizarMetodoPago(id, metodoPago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
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
