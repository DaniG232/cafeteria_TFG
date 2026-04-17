package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.Encuesta;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.service.IServicioEncuestas;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestion de encuestas.
 */
@RestController
@RequestMapping("/encuestas")
public class EncuestaController {

    private final IServicioEncuestas servicioEncuestas;

    public EncuestaController(IServicioEncuestas servicioEncuestas) {
        this.servicioEncuestas = servicioEncuestas;
    }

    @GetMapping
    public ResponseEntity<List<Encuesta>> listarEncuestas() {
        return ResponseEntity.ok(servicioEncuestas.listarEncuestas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> verEncuesta(@PathVariable Integer id,
                                                           @AuthenticationPrincipal Usuario usuario) {
        Encuesta encuesta = servicioEncuestas.buscarPorId(id);
        boolean yaRellenada = servicioEncuestas.haRellenado(usuario.getEmail(), id);
        long totalRespuestas = servicioEncuestas.contarRespuestasEncuesta(id);

        Map<String, Object> model = new HashMap<>();
        model.put("encuesta", encuesta);
        model.put("yaRellenada", yaRellenada);
        model.put("totalRespuestas", totalRespuestas);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/{id}/rellenar")
    public ResponseEntity<Map<String, String>> rellenarEncuesta(@PathVariable Integer id,
                                                                @AuthenticationPrincipal Usuario usuario) {
        try {
            servicioEncuestas.rellenarEncuesta(usuario.getEmail(), id);
            return ResponseEntity.ok(Map.of("mensaje", "Encuesta completada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
