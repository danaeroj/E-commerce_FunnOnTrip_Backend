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
    public Contacto obtenerPorId(Integer id) {
        return contactoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
    }

    public Contacto actualizar(Integer id, Contacto contacto) {
        Contacto existente = obtenerPorId(id);
        existente.setNombre(contacto.getNombre());
        existente.setCorreoElectronico(contacto.getCorreoElectronico());
        existente.setTelefono(contacto.getTelefono());
        existente.setAsunto(contacto.getAsunto());
        existente.setMensaje(contacto.getMensaje());
        existente.setAtendido(contacto.getAtendido());
        return contactoRepository.save(existente);
    }

}
