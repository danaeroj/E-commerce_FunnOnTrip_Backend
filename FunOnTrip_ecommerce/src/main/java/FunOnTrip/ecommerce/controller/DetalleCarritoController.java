package FunOnTrip.ecommerce.controller;

import FunOnTrip.ecommerce.model.DetalleCarrito;
import FunOnTrip.ecommerce.service.DetalleCarritoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-carrito")
public class DetalleCarritoController {

    private final DetalleCarritoService detalleService;

    public DetalleCarritoController(DetalleCarritoService detalleService) {
        this.detalleService = detalleService;
    }

    @GetMapping("/carrito/{carritoId}")
    public List<DetalleCarrito> getDetallesByCarrito(@PathVariable Integer carritoId) {
        return detalleService.getDetallesByCarrito(carritoId);
    }

    @GetMapping("/{id}")
    public DetalleCarrito getDetalleById(@PathVariable Integer id) {
        return detalleService.getDetalleById(id);
    }

    @PostMapping
    public DetalleCarrito addProducto(
            @RequestParam Integer carritoId,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {

        return detalleService.addProducto(carritoId, productoId, cantidad);
    }

    @PutMapping("/{id}")
    public DetalleCarrito updateCantidad(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {

        return detalleService.updateCantidad(id, cantidad);
    }

    @DeleteMapping("/{id}")
    public void deleteDetalle(@PathVariable Integer id) {
        detalleService.deleteDetalle(id);
    }
}


