package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioUsuarios;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para registro de usuarios y gestion de perfil.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IServicioUsuarios servicioUsuarios;

    public UsuarioController(IServicioUsuarios servicioUsuarios) {
        this.servicioUsuarios = servicioUsuarios;
    }

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            servicioUsuarios.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Registro exitoso."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
