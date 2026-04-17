package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.*;
import com.dani.cafeteria.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Panel de administracion exclusivo para cocineros.
 * Gestion de platos, menus, encuestas y reservas.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IServicioPlatos servicioPlatos;
    private final IServicioMenus servicioMenus;
    private final IServicioEncuestas servicioEncuestas;
    private final IServicioReservas servicioReservas;
    private final IServicioUsuarios servicioUsuarios;

    public AdminController(IServicioPlatos servicioPlatos,
                           IServicioMenus servicioMenus,
                           IServicioEncuestas servicioEncuestas,
                           IServicioReservas servicioReservas,
                           IServicioUsuarios servicioUsuarios) {
        this.servicioPlatos = servicioPlatos;
        this.servicioMenus = servicioMenus;
        this.servicioEncuestas = servicioEncuestas;
        this.servicioReservas = servicioReservas;
        this.servicioUsuarios = servicioUsuarios;
    }

    // ========== PANEL PRINCIPAL ==========

    @GetMapping
    public String panelAdmin(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalPlatos", servicioPlatos.listarPlatos().size());
        model.addAttribute("totalMenus", servicioMenus.listarMenusProximos().size());
        model.addAttribute("totalEncuestas", servicioEncuestas.listarEncuestas().size());
        return "admin/panel";
    }

    // ==================== PLATOS ====================

    @GetMapping("/platos")
    public String listarPlatos(Model model) {
        model.addAttribute("platos", servicioPlatos.listarPlatos());
        return "admin/platos/lista";
    }

    @GetMapping("/platos/nuevo")
    public String formularioPlato(Model model) {
        model.addAttribute("plato", new Plato());
        model.addAttribute("tipos", Plato.TipoPlato.values());
        return "admin/platos/formulario";
    }

    @PostMapping("/platos/nuevo")
    public String crearPlato(@ModelAttribute Plato plato,
                             RedirectAttributes redirectAttributes) {
        try {
            servicioPlatos.crearPlato(plato);
            redirectAttributes.addFlashAttribute("mensaje", "Plato creado correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/platos";
    }

    @GetMapping("/platos/{id}/editar")
    public String editarPlatoForm(@PathVariable Integer id, Model model) {
        model.addAttribute("plato", servicioPlatos.buscarPorId(id));
        model.addAttribute("tipos", Plato.TipoPlato.values());
        return "admin/platos/formulario";
    }

    @PostMapping("/platos/{id}/editar")
    public String editarPlato(@PathVariable Integer id,
                              @ModelAttribute Plato plato,
                              RedirectAttributes redirectAttributes) {
        try {
            servicioPlatos.actualizarPlato(id, plato);
            redirectAttributes.addFlashAttribute("mensaje", "Plato actualizado");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/platos";
    }

    @PostMapping("/platos/{id}/desactivar")
    public String desactivarPlato(@PathVariable Integer id,
                                  RedirectAttributes redirectAttributes) {
        servicioPlatos.desactivarPlato(id);
        redirectAttributes.addFlashAttribute("mensaje", "Plato desactivado");
        return "redirect:/admin/platos";
    }

    // ==================== MENUS ====================

    @GetMapping("/menus")
    public String listarMenusAdmin(Model model) {
        model.addAttribute("menus", servicioMenus.listarMenusProximos());
        return "admin/menus/lista";
    }

    @GetMapping("/menus/nuevo")
    public String formularioMenu(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("primeros", servicioPlatos.listarPorTipo(Plato.TipoPlato.PRIMERO));
        model.addAttribute("segundos", servicioPlatos.listarPorTipo(Plato.TipoPlato.SEGUNDO));
        model.addAttribute("postres", servicioPlatos.listarPorTipo(Plato.TipoPlato.POSTRE));
        model.addAttribute("bebidas", servicioPlatos.listarPorTipo(Plato.TipoPlato.BEBIDA));
        return "admin/menus/formulario";
    }

    @PostMapping("/menus/nuevo")
    public String crearMenu(@RequestParam String fecha,
                            @RequestParam Integer primerPlato,
                            @RequestParam Integer segundoPlato,
                            @RequestParam Integer postre,
                            @RequestParam Integer bebida,
                            RedirectAttributes redirectAttributes) {
        try {
            Menu menu = Menu.builder()
                    .fecha(java.time.LocalDate.parse(fecha))
                    .primerPlato(servicioPlatos.buscarPorId(primerPlato))
                    .segundoPlato(servicioPlatos.buscarPorId(segundoPlato))
                    .postre(servicioPlatos.buscarPorId(postre))
                    .bebida(servicioPlatos.buscarPorId(bebida))
                    .build();
            servicioMenus.crearMenu(menu);
            redirectAttributes.addFlashAttribute("mensaje", "Menu creado correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/menus";
    }

    @PostMapping("/menus/{id}/eliminar")
    public String eliminarMenu(@PathVariable Integer id,
                               RedirectAttributes redirectAttributes) {
        try {
            servicioMenus.eliminarMenu(id);
            redirectAttributes.addFlashAttribute("mensaje", "Menu eliminado");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/menus";
    }

    // ==================== ENCUESTAS ====================

    @GetMapping("/encuestas")
    public String listarEncuestasAdmin(Model model) {
        model.addAttribute("encuestas", servicioEncuestas.listarEncuestas());
        return "admin/encuestas/lista";
    }

    @GetMapping("/encuestas/nueva")
    public String formularioEncuesta(Model model) {
        model.addAttribute("encuesta", new Encuesta());
        return "admin/encuestas/formulario";
    }

    @PostMapping("/encuestas/nueva")
    public String crearEncuesta(@ModelAttribute Encuesta encuesta,
                                RedirectAttributes redirectAttributes) {
        try {
            servicioEncuestas.crearEncuesta(encuesta);
            redirectAttributes.addFlashAttribute("mensaje", "Encuesta creada correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/encuestas";
    }

    @PostMapping("/encuestas/{id}/eliminar")
    public String eliminarEncuesta(@PathVariable Integer id,
                                   RedirectAttributes redirectAttributes) {
        try {
            servicioEncuestas.eliminarEncuesta(id);
            redirectAttributes.addFlashAttribute("mensaje", "Encuesta eliminada");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/encuestas";
    }

    // ==================== RESERVAS (vista cocinero) ====================

    @GetMapping("/reservas")
    public String listarReservasAdmin(Model model) {
        List<Menu> menus = servicioMenus.listarMenusProximos();
        model.addAttribute("menus", menus);
        return "admin/reservas/lista";
    }

    @GetMapping("/reservas/menu/{idMenu}")
    public String verReservasMenu(@PathVariable Integer idMenu, Model model) {
        Menu menu = servicioMenus.buscarPorId(idMenu);
        model.addAttribute("menu", menu);
        model.addAttribute("reservas", servicioReservas.listarReservasMenu(idMenu));
        return "admin/reservas/detalle";
    }

    @PostMapping("/reservas/{id}/asistida")
    public String marcarAsistida(@PathVariable Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Reserva reserva = servicioReservas.buscarPorId(id);
            servicioReservas.marcarAsistida(id);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva marcada como recogida");
            return "redirect:/admin/reservas/menu/" + reserva.getMenu().getIdMenu();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/reservas";
        }
    }

    // ==================== USUARIOS ====================

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", servicioUsuarios.listarUsuarios());
        return "admin/usuarios/lista";
    }

    @PostMapping("/usuarios/{email}/desactivar")
    public String desactivarUsuario(@PathVariable String email,
                                    RedirectAttributes redirectAttributes) {
        servicioUsuarios.desactivarUsuario(email);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{email}/activar")
    public String activarUsuario(@PathVariable String email,
                                 RedirectAttributes redirectAttributes) {
        servicioUsuarios.activarUsuario(email);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario activado");
        return "redirect:/admin/usuarios";
    }
}
