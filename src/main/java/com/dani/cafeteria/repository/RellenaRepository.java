package com.dani.cafeteria.repository;

import com.dani.cafeteria.entity.Rellena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RellenaRepository extends JpaRepository<Rellena, Rellena.RellenaId> {

    List<Rellena> findByUsuarioEmail(String email);

    List<Rellena> findByEncuestaIdEncuesta(Integer idEncuesta);

    Optional<Rellena> findByUsuarioEmailAndEncuestaIdEncuesta(String email, Integer idEncuesta);
}
