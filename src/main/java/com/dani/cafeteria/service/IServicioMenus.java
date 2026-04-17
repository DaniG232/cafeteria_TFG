package com.dani.cafeteria.service;

import com.dani.cafeteria.entity.Menu;

import java.time.LocalDate;
import java.util.List;

public interface IServicioMenus {

    Menu crearMenu(Menu menu);

    Menu buscarPorId(Integer id);

    Menu buscarPorFecha(LocalDate fecha);

    Menu buscarMenuHoy();

    List<Menu> listarMenusProximos();

    List<Menu> listarMenusSemana(LocalDate inicio, LocalDate fin);

    Menu actualizarMenu(Integer id, Menu menu);

    void eliminarMenu(Integer id);
}
