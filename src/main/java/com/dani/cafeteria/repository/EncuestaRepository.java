package com.dani.cafeteria.repository;

import com.dani.cafeteria.entity.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EncuestaRepository extends JpaRepository<Encuesta, Integer> {

    List<Encuesta> findAllByOrderByFechaCreacionDesc();
}
