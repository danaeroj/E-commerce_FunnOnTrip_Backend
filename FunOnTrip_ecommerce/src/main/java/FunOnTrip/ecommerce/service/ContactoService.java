package FunOnTrip.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import FunOnTrip.ecommerce.model.Contacto;
import FunOnTrip.ecommerce.repository.ContactoRepository;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;

    @Autowired
    public ContactoService(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    /**
     * Crear nuevo contacto
     */
    @Transactional
    public Contacto crear(Contacto contacto) {
        return contactoRepository.save(contacto);
    }

    /**
     * Obtener todos los contactos
     */
    public List<Contacto> obtenerTodos() {
        return contactoRepository.findAll();
    }

    /**
     * Obtener contactos no atendidos
     */
    public List<Contacto> obtenerNoAtendidos() {
        return contactoRepository.findByAtendidoFalse();
    }

    /**
     * Obtener contacto por ID
     */
    public Contacto obtenerPorId(Integer id) {
        return contactoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Contacto con id [" + id + "] no encontrado"));
    }

    /**
     * Actualizar contacto
     */
    @Transactional
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

    /**
     * Eliminar contacto
     */
    @Transactional
    public void eliminar(Integer id) {
        if (!contactoRepository.existsById(id)) {
            throw new IllegalArgumentException("Contacto con id [" + id + "] no existe");
        }
        contactoRepository.deleteById(id);
    }

    /**
     * Marcar contacto como atendido
     */
    @Transactional
    public Contacto marcarComoAtendido(Integer id) {
        Contacto contacto = obtenerPorId(id);
        contacto.setAtendido(true);
        return contactoRepository.save(contacto);
    }
}