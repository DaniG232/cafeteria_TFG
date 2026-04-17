package com.dani.cafeteria.repository;

import com.dani.cafeteria.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    Optional<Menu> findByFecha(LocalDate fecha);

    List<Menu> findByFechaBetween(LocalDate inicio, LocalDate fin);

    List<Menu> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);
}
