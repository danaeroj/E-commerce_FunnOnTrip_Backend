package FunOnTrip.ecommerce.controller;


import FunOnTrip.ecommerce.model.Pedido;
import FunOnTrip.ecommerce.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST: Pedido
 *
 * Endpoints principales:
 * - POST /api/pedidos               crea pedido con detalles (flujo principal)
 * - GET  /api/pedidos/{id}          obtiene pedido (con detalles)
 * - GET  /api/pedidos               lista pedidos
 * - GET  /api/pedidos/usuario/{id}  lista pedidos por usuario
 * - PUT  /api/pedidos/{id}/estado   cambia estado (flujo de pedidos)
 * - DELETE /api/pedidos/{id}        elimina pedido
 */
@RestController
@RequestMapping("/api/pedidos")
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
     * Crea un pedido con sus detalles.
     *
     * Request ejemplo:
     * {
     *   "usuarioId": 1,
     *   "metodoPago": "tarjeta",
     *   "items": [
     *     {"productoId": 2, "cantidad": 1},
     *     {"productoId": 5, "cantidad": 3}
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<Pedido> create(@RequestBody CreatePedidoRequest request) {
        Pedido creado = pedidoService.crearPedido(request.usuarioId, request.metodoPago, request.items);
        return ResponseEntity.ok(creado);
    }

    /**
     * Actualiza el estado del pedido.
     * Ejemplo: PUT /api/pedidos/10/estado?estado=pagado
     */
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

    /**
     * DTO interno para crear pedidos (evita crear archivos extra).
     */
    public static class CreatePedidoRequest {
        public Integer usuarioId;
        public String metodoPago;
        public List<PedidoService.ItemPedido> items;

        public CreatePedidoRequest() {}
    }
}

