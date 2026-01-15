package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    List<Contacto> findByAtendidoFalse();
}