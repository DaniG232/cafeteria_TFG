package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Menu;
import com.dani.cafeteria.entity.Reserva;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioMenus;
import com.dani.cafeteria.service.IServicioReservas;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestion de menus y reservas.
 * Los profesores ven menus y hacen reservas.
 */
@RestController
@RequestMapping("/menus")
public class MenuController {

    private final IServicioMenus servicioMenus;
    private final IServicioReservas servicioReservas;

    public MenuController(IServicioMenus servicioMenus,
                          IServicioReservas servicioReservas) {
        this.servicioMenus = servicioMenus;
        this.servicioReservas = servicioReservas;
    }

    // ========== VER MENUS ==========

    @GetMapping
    public ResponseEntity<List<Menu>> listarMenus() {
        List<Menu> menus = servicioMenus.listarMenusProximos();
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> verMenu(@PathVariable Integer id,
                                                       @AuthenticationPrincipal Usuario usuario) {
        Menu menu = servicioMenus.buscarPorId(id);
        boolean tieneReserva = servicioReservas.existeReserva(usuario.getEmail(), id);
        List<Reserva> reservas = servicioReservas.listarReservasMenu(id);

        Map<String, Object> model = new HashMap<>();
        model.put("menu", menu);
        model.put("tieneReserva", tieneReserva);
        model.put("reservasMenu", reservas);

        return ResponseEntity.ok(model);
    }

    // ========== RESERVAR ==========

    @PostMapping("/{id}/reservar")
    public ResponseEntity<Map<String, String>> reservarMenu(@PathVariable Integer id,
                                                            @AuthenticationPrincipal Usuario usuario) {
        try {
            servicioReservas.crearReserva(usuario.getEmail(), id);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva realizada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ========== CANCELAR RESERVA ==========

    @PostMapping("/reservas/{idReserva}/cancelar")
    public ResponseEntity<Map<String, String>> cancelarReserva(@PathVariable Integer idReserva) {
        try {
            servicioReservas.cancelarReserva(idReserva);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
