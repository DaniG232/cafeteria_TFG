package com.dani.cafeteria.service;

import com.dani.cafeteria.entity.Plato;

import java.util.List;

public interface IServicioPlatos {

    Plato crearPlato(Plato plato);

    Plato buscarPorId(Integer id);

    List<Plato> listarPlatos();

    List<Plato> listarPlatosActivos();

    List<Plato> listarPorTipo(Plato.TipoPlato tipo);

    Plato actualizarPlato(Integer id, Plato plato);

    void desactivarPlato(Integer id);
}
