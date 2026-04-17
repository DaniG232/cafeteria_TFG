package com.dani.cafeteria.service;

import com.dani.cafeteria.entity.Reserva;

import java.util.List;

public interface IServicioReservas {

    Reserva crearReserva(String emailUsuario, Integer idMenu);

    Reserva buscarPorId(Integer id);

    List<Reserva> listarReservasUsuario(String email);

    List<Reserva> listarReservasMenu(Integer idMenu);

    void cancelarReserva(Integer idReserva);

    void marcarAsistida(Integer idReserva);

    boolean existeReserva(String email, Integer idMenu);
}
