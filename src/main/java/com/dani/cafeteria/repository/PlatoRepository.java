package com.dani.cafeteria.repository;

import com.dani.cafeteria.entity.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Integer> {

    List<Plato> findByActivoTrue();

    List<Plato> findByTipo(Plato.TipoPlato tipo);
}
