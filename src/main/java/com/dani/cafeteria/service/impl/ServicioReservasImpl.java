package com.dani.cafeteria.service.impl;

import com.dani.cafeteria.entity.Menu;
import com.dani.cafeteria.entity.Reserva;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.repository.ReservaRepository;
import com.dani.cafeteria.service.IServicioMenus;
import com.dani.cafeteria.service.IServicioReservas;
import com.dani.cafeteria.service.IServicioUsuarios;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ServicioReservasImpl implements IServicioReservas {

    private final ReservaRepository reservaRepository;
    private final IServicioUsuarios servicioUsuarios;
    private final IServicioMenus servicioMenus;

    public ServicioReservasImpl(ReservaRepository reservaRepository,
                                IServicioUsuarios servicioUsuarios,
                                IServicioMenus servicioMenus) {
        this.reservaRepository = reservaRepository;
        this.servicioUsuarios = servicioUsuarios;
        this.servicioMenus = servicioMenus;
    }

    @Override
    public Reserva crearReserva(String emailUsuario, Integer idMenu) {
        Usuario usuario = servicioUsuarios.buscarPorEmail(emailUsuario);
        Menu menu = servicioMenus.buscarPorId(idMenu);

        // Comprobar que no tenga ya una reserva para ese menu
        if (reservaRepository.findByUsuarioAndMenu(usuario, menu).isPresent()) {
            throw new RuntimeException("Ya tienes una reserva para este menu");
        }

        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .menu(menu)
                .fechaReserva(LocalDate.now())
                .estado("PENDIENTE")
                .codigoRecogida(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .asistido(false)
                .build();

        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva buscarPorId(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + id));
    }

    @Override
    public List<Reserva> listarReservasUsuario(String email) {
        return reservaRepository.findByUsuarioEmailOrderByFechaReservaDesc(email);
    }

    @Override
    public List<Reserva> listarReservasMenu(Integer idMenu) {
        Menu menu = servicioMenus.buscarPorId(idMenu);
        return reservaRepository.findByMenu(menu);
    }

    @Override
    public void cancelarReserva(Integer idReserva) {
        Reserva reserva = buscarPorId(idReserva);
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }

    @Override
    public void marcarAsistida(Integer idReserva) {
        Reserva reserva = buscarPorId(idReserva);
        reserva.setAsistido(true);
        reserva.setEstado("RECOGIDA");
        reservaRepository.save(reserva);
    }

    @Override
    public boolean existeReserva(String email, Integer idMenu) {
        Usuario usuario = servicioUsuarios.buscarPorEmail(email);
        Menu menu = servicioMenus.buscarPorId(idMenu);
        return reservaRepository.findByUsuarioAndMenu(usuario, menu).isPresent();
    }
}
