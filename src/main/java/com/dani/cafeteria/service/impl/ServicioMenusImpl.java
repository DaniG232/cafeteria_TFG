package com.dani.cafeteria.service.impl;

import com.dani.cafeteria.entity.Menu;
import com.dani.cafeteria.repository.MenuRepository;
import com.dani.cafeteria.service.IServicioMenus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioMenusImpl implements IServicioMenus {

    private final MenuRepository menuRepository;

    public ServicioMenusImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu crearMenu(Menu menu) {
        // Comprobar que no exista ya un menu para esa fecha
        if (menuRepository.findByFecha(menu.getFecha()).isPresent()) {
            throw new RuntimeException("Ya existe un menu para la fecha: " + menu.getFecha());
        }
        return menuRepository.save(menu);
    }

    @Override
    public Menu buscarPorId(Integer id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu no encontrado: " + id));
    }

    @Override
    public Menu buscarPorFecha(LocalDate fecha) {
        return menuRepository.findByFecha(fecha)
                .orElseThrow(() -> new RuntimeException("No hay menu para la fecha: " + fecha));
    }

    @Override
    public Menu buscarMenuHoy() {
        return menuRepository.findByFecha(LocalDate.now())
                .orElse(null);
    }

    @Override
    public List<Menu> listarMenusProximos() {
        return menuRepository.findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate.now());
    }

    @Override
    public List<Menu> listarMenusSemana(LocalDate inicio, LocalDate fin) {
        return menuRepository.findByFechaBetween(inicio, fin);
    }

    @Override
    public Menu actualizarMenu(Integer id, Menu menu) {
        Menu existente = buscarPorId(id);
        existente.setFecha(menu.getFecha());
        existente.setPrimerPlato(menu.getPrimerPlato());
        existente.setSegundoPlato(menu.getSegundoPlato());
        existente.setPostre(menu.getPostre());
        existente.setBebida(menu.getBebida());
        return menuRepository.save(existente);
    }

    @Override
    public void eliminarMenu(Integer id) {
        Menu menu = buscarPorId(id);
        menuRepository.delete(menu);
    }
}
