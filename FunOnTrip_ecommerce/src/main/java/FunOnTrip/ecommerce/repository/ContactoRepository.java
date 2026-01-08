package FunOnTrip.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import FunOnTrip.ecommerce.model.Contacto;

public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
}
