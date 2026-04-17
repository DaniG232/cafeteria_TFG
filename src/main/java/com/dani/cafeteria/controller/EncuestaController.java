package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Encuesta;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioEncuestas;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestion de encuestas.
 */
@Controller
@RequestMapping("/encuestas")
public class EncuestaController {

    private final IServicioEncuestas servicioEncuestas;

    public EncuestaController(IServicioEncuestas servicioEncuestas) {
        this.servicioEncuestas = servicioEncuestas;
    }

    @GetMapping
    public String listarEncuestas(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("encuestas", servicioEncuestas.listarEncuestas());
        model.addAttribute("usuario", usuario);
        return "encuestas/lista";
    }

    @GetMapping("/{id}")
    public String verEncuesta(@PathVariable Integer id,
                              @AuthenticationPrincipal Usuario usuario,
                              Model model) {
        Encuesta encuesta = servicioEncuestas.buscarPorId(id);
        model.addAttribute("encuesta", encuesta);
        model.addAttribute("usuario", usuario);

        boolean yaRellenada = servicioEncuestas.haRellenado(usuario.getEmail(), id);
        model.addAttribute("yaRellenada", yaRellenada);

        long totalRespuestas = servicioEncuestas.contarRespuestasEncuesta(id);
        model.addAttribute("totalRespuestas", totalRespuestas);

        return "encuestas/detalle";
    }

    @PostMapping("/{id}/rellenar")
    public String rellenarEncuesta(@PathVariable Integer id,
                                   @AuthenticationPrincipal Usuario usuario,
                                   RedirectAttributes redirectAttributes) {
        try {
            servicioEncuestas.rellenarEncuesta(usuario.getEmail(), id);
            redirectAttributes.addFlashAttribute("mensaje", "Encuesta completada");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/encuestas/" + id;
    }
}
