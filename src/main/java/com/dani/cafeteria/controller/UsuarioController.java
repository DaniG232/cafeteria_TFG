package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Rol;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioUsuarios;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para registro de usuarios y gestion de perfil.
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IServicioUsuarios servicioUsuarios;

    public UsuarioController(IServicioUsuarios servicioUsuarios) {
        this.servicioUsuarios = servicioUsuarios;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario,
                                    RedirectAttributes redirectAttributes) {
        try {
            servicioUsuarios.crearUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Ya puedes iniciar sesion.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios/registro";
        }
    }
}
