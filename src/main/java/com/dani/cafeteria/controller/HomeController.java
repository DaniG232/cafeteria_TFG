package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Menu;
import com.dani.cafeteria.entity.Reserva;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioEncuestas;
import com.dani.cafeteria.service.IServicioMenus;
import com.dani.cafeteria.service.IServicioReservas;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador principal: pagina de inicio, login y home.
 */
@RestController
public class HomeController {

    private final IServicioMenus servicioMenus;
    private final IServicioReservas servicioReservas;
    private final IServicioEncuestas servicioEncuestas;

    public HomeController(IServicioMenus servicioMenus,
                          IServicioReservas servicioReservas,
                          IServicioEncuestas servicioEncuestas) {
        this.servicioMenus = servicioMenus;
        this.servicioReservas = servicioReservas;
        this.servicioEncuestas = servicioEncuestas;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> inicio() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bienvenido a la API de la Cafeteria");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> home(@AuthenticationPrincipal Usuario usuario) {
        Map<String, Object> model = new HashMap<>();
        model.put("usuario", usuario);

        // Menu del dia
        Menu menuHoy = servicioMenus.buscarMenuHoy();
        model.put("menuHoy", menuHoy);

        // Menus proximos
        List<Menu> menusProximos = servicioMenus.listarMenusProximos();
        model.put("menusProximos", menusProximos);

        // Reservas del usuario
        List<Reserva> misReservas = servicioReservas.listarReservasUsuario(usuario.getEmail());
        model.put("misReservas", misReservas);

        // Encuestas disponibles
        model.put("encuestas", servicioEncuestas.listarEncuestas());

        return ResponseEntity.ok(model);
    }
}
