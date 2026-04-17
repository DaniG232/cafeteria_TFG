package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Menu;
import com.dani.cafeteria.entity.Reserva;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioEncuestas;
import com.dani.cafeteria.service.IServicioMenus;
import com.dani.cafeteria.service.IServicioReservas;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controlador principal: pagina de inicio, login y home.
 */
@Controller
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
    public String inicio() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("usuario", usuario);

        // Menu del dia
        Menu menuHoy = servicioMenus.buscarMenuHoy();
        model.addAttribute("menuHoy", menuHoy);

        // Menus proximos
        List<Menu> menusProximos = servicioMenus.listarMenusProximos();
        model.addAttribute("menusProximos", menusProximos);

        // Reservas del usuario
        List<Reserva> misReservas = servicioReservas.listarReservasUsuario(usuario.getEmail());
        model.addAttribute("misReservas", misReservas);

        // Encuestas disponibles
        model.addAttribute("encuestas", servicioEncuestas.listarEncuestas());

        return "home";
    }
}
