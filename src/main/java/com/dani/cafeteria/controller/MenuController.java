package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Menu;
import com.dani.cafeteria.entity.Reserva;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioMenus;
import com.dani.cafeteria.service.IServicioReservas;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para la gestion de menus y reservas.
 * Los profesores ven menus y hacen reservas.
 */
@Controller
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
    public String listarMenus(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<Menu> menus = servicioMenus.listarMenusProximos();
        model.addAttribute("menus", menus);
        model.addAttribute("usuario", usuario);
        return "menus/lista";
    }

    @GetMapping("/{id}")
    public String verMenu(@PathVariable Integer id,
                          @AuthenticationPrincipal Usuario usuario,
                          Model model) {
        Menu menu = servicioMenus.buscarPorId(id);
        model.addAttribute("menu", menu);
        model.addAttribute("usuario", usuario);

        // Comprobar si ya tiene reserva
        boolean tieneReserva = servicioReservas.existeReserva(usuario.getEmail(), id);
        model.addAttribute("tieneReserva", tieneReserva);

        // Reservas de este menu (para cocineros)
        List<Reserva> reservas = servicioReservas.listarReservasMenu(id);
        model.addAttribute("reservasMenu", reservas);

        return "menus/detalle";
    }

    // ========== RESERVAR ==========

    @PostMapping("/{id}/reservar")
    public String reservarMenu(@PathVariable Integer id,
                               @AuthenticationPrincipal Usuario usuario,
                               RedirectAttributes redirectAttributes) {
        try {
            servicioReservas.crearReserva(usuario.getEmail(), id);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva realizada correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/menus/" + id;
    }

    // ========== CANCELAR RESERVA ==========

    @PostMapping("/reservas/{idReserva}/cancelar")
    public String cancelarReserva(@PathVariable Integer idReserva,
                                  RedirectAttributes redirectAttributes) {
        try {
            servicioReservas.cancelarReserva(idReserva);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva cancelada");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }
}
