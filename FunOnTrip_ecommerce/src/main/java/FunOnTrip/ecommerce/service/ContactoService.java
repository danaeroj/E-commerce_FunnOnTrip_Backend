package FunOnTrip.ecommerce.service;

import java.util.List;
import org.springframework.stereotype.Service;
import FunOnTrip.ecommerce.model.Contacto;
import FunOnTrip.ecommerce.repository.ContactoRepository;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;

    public ContactoService(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    public Contacto crear(Contacto contacto) {
        return contactoRepository.save(contacto);
    }

    public List<Contacto> obtenerTodos() {
        return contactoRepository.findAll();
    }

    public void eliminar(Integer id) {
        contactoRepository.deleteById(id);
    }
}
