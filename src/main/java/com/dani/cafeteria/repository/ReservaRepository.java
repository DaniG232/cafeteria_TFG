package com.dani.cafeteria.repository;

import com.dani.cafeteria.entity.Reserva;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByUsuario(Usuario usuario);

    List<Reserva> findByMenu(Menu menu);

    Optional<Reserva> findByUsuarioAndMenu(Usuario usuario, Menu menu);

    List<Reserva> findByUsuarioEmailOrderByFechaReservaDesc(String email);
}
