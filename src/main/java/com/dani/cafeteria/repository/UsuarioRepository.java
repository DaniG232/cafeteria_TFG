package com.dani.cafeteria.repository;

import com.dani.cafeteria.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    List<Usuario> findByEstadoTrue();
}
