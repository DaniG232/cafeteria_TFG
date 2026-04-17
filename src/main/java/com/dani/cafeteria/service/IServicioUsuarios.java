package com.dani.cafeteria.service;

import com.dani.cafeteria.entity.Usuario;

import java.util.List;

public interface IServicioUsuarios {

    Usuario crearUsuario(Usuario usuario);

    Usuario buscarPorEmail(String email);

    List<Usuario> listarUsuarios();

    List<Usuario> listarUsuariosActivos();

    void desactivarUsuario(String email);

    void activarUsuario(String email);

    boolean existeUsuario(String email);
}
