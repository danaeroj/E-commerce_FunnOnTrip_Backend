package FunOnTrip.ecommerce.controller;


import FunOnTrip.ecommerce.model.DetallePedido;
import FunOnTrip.ecommerce.service.DetallePedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST: DetallePedido
 *
 * Nota: aunque en un flujo real los detalles se manejan dentro de "crearPedido",
 * aquí se exponen endpoints por requerimiento de CRUD completo.
 */
@RestController
@RequestMapping("/api/detalles-pedidos")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedido> getAll() {
        return detallePedidoService.getAll();
    }

    @GetMapping("/{id}")
    public DetallePedido getById(@PathVariable Integer id) {
        return detallePedidoService.getById(id);
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<DetallePedido> getByPedido(@PathVariable Integer pedidoId) {
        return detallePedidoService.getByPedido(pedidoId);
    }

    /**
     * POST /api/detalles-pedidos
     * {
     *   "pedidoId": 10,
     *   "productoId": 3,
     *   "cantidad": 2
     * }
     */
    @PostMapping
    public ResponseEntity<DetallePedido> create(@RequestBody CreateDetallePedidoRequest request) {
        DetallePedido creado = detallePedidoService.create(request.pedidoId, request.productoId, request.cantidad);
        return ResponseEntity.ok(creado);
    }

    /**
     * PUT /api/detalles-pedidos/{id}/cantidad?nuevaCantidad=5
     */
    @PutMapping("/{id}/cantidad")
    public DetallePedido updateCantidad(@PathVariable Integer id, @RequestParam Integer nuevaCantidad) {
        return detallePedidoService.updateCantidad(id, nuevaCantidad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        detallePedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public static class CreateDetallePedidoRequest {
        public Integer pedidoId;
        public Integer productoId;
        public Integer cantidad;

        public CreateDetallePedidoRequest() {}
    }
}
