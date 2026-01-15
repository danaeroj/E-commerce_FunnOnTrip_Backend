package FunOnTrip.ecommerce.repository;

import FunOnTrip.ecommerce.model.Carrito;

import FunOnTrip.ecommerce.model.EstadoCarrito;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

	    Optional<Carrito> findByUsuario_IdAndEstado(Long usuarioId, EstadoCarrito estado);
	}

