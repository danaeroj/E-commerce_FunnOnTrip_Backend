package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

  @Query("SELECT DISTINCT c FROM Carrito c " +
         "LEFT JOIN FETCH c.detalles d " +
         "LEFT JOIN FETCH d.producto " +
         "WHERE c.idCarrito = :id")
  Optional<Carrito> findByIdWithDetalles(@Param("id") Integer id);

  @Query("SELECT DISTINCT c FROM Carrito c " +
         "LEFT JOIN FETCH c.detalles d " +
         "LEFT JOIN FETCH d.producto")
  List<Carrito> findAllWithDetalles();

  Optional<Carrito> findByUsuario_IdAndEstado(Long usuarioId, Carrito.EstadoCarrito estado);
}
