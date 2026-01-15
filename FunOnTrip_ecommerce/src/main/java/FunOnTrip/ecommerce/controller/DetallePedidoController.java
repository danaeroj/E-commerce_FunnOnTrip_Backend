package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.DetallePedido;
import FunOnTrip.ecommerce.service.DetallePedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<DetallePedido> create(@RequestBody CreateDetallePedidoRequest request) {
        return ResponseEntity.ok(
                detallePedidoService.create(request.pedidoId, request.productoId, request.cantidad)
        );
    }

    @PutMapping("/{id}/cantidad")
    public DetallePedido updateCantidad(
            @PathVariable Integer id,
            @RequestParam Integer nuevaCantidad) {

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
    }
}

