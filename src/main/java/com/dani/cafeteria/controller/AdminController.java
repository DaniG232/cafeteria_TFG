package com.dani.cafeteria.controller;

import com.dani.cafeteria.entity.*;
import com.dani.cafeteria.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel de administracion exclusivo para cocineros.
 * Gestion de platos, menus, encuestas y reservas.
 */
@RestController
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
    public ResponseEntity<Map<String, Object>> panelAdmin(@AuthenticationPrincipal Usuario usuario) {
        Map<String, Object> model = new HashMap<>();
        model.put("usuario", usuario);
        model.put("totalPlatos", servicioPlatos.listarPlatos().size());
        model.put("totalMenus", servicioMenus.listarMenusProximos().size());
        model.put("totalEncuestas", servicioEncuestas.listarEncuestas().size());
        return ResponseEntity.ok(model);
    }

    // ==================== PLATOS ====================

    @GetMapping("/platos")
    public ResponseEntity<List<Plato>> listarPlatos() {
        return ResponseEntity.ok(servicioPlatos.listarPlatos());
    }

    @PostMapping("/platos")
    public ResponseEntity<Map<String, String>> crearPlato(@RequestBody Plato plato) {
        try {
            servicioPlatos.crearPlato(plato);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Plato creado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/platos/{id}")
    public ResponseEntity<Map<String, String>> editarPlato(@PathVariable Integer id,
                                                           @RequestBody Plato plato) {
        try {
            servicioPlatos.actualizarPlato(id, plato);
            return ResponseEntity.ok(Map.of("mensaje", "Plato actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/platos/{id}/desactivar")
    public ResponseEntity<Map<String, String>> desactivarPlato(@PathVariable Integer id) {
        servicioPlatos.desactivarPlato(id);
        return ResponseEntity.ok(Map.of("mensaje", "Plato desactivado"));
    }

    // ==================== MENUS ====================

    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> listarMenusAdmin() {
        return ResponseEntity.ok(servicioMenus.listarMenusProximos());
    }

    @PostMapping("/menus")
    public ResponseEntity<Map<String, String>> crearMenu(@RequestBody Menu menu) {
        try {
            servicioMenus.crearMenu(menu);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Menu creado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Map<String, String>> eliminarMenu(@PathVariable Integer id) {
        try {
            servicioMenus.eliminarMenu(id);
            return ResponseEntity.ok(Map.of("mensaje", "Menu eliminado"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== ENCUESTAS ====================

    @GetMapping("/encuestas")
    public ResponseEntity<List<Encuesta>> listarEncuestasAdmin() {
        return ResponseEntity.ok(servicioEncuestas.listarEncuestas());
    }

    @PostMapping("/encuestas")
    public ResponseEntity<Map<String, String>> crearEncuesta(@RequestBody Encuesta encuesta) {
        try {
            servicioEncuestas.crearEncuesta(encuesta);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Encuesta creada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/encuestas/{id}")
    public ResponseEntity<Map<String, String>> eliminarEncuesta(@PathVariable Integer id) {
        try {
            servicioEncuestas.eliminarEncuesta(id);
            return ResponseEntity.ok(Map.of("mensaje", "Encuesta eliminada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== RESERVAS (vista cocinero) ====================

    @GetMapping("/reservas")
    public ResponseEntity<List<Menu>> listarReservasAdmin() {
        List<Menu> menus = servicioMenus.listarMenusProximos();
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/reservas/menu/{idMenu}")
    public ResponseEntity<Map<String, Object>> verReservasMenu(@PathVariable Integer idMenu) {
        Menu menu = servicioMenus.buscarPorId(idMenu);
        List<Reserva> reservas = servicioReservas.listarReservasMenu(idMenu);
        Map<String, Object> model = new HashMap<>();
        model.put("menu", menu);
        model.put("reservas", reservas);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/reservas/{id}/asistida")
    public ResponseEntity<Map<String, String>> marcarAsistida(@PathVariable Integer id) {
        try {
            servicioReservas.marcarAsistida(id);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva marcada como recogida"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== USUARIOS ====================

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(servicioUsuarios.listarUsuarios());
    }

    @PostMapping("/usuarios/{email}/desactivar")
    public ResponseEntity<Map<String, String>> desactivarUsuario(@PathVariable String email) {
        servicioUsuarios.desactivarUsuario(email);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario desactivado"));
    }

    @PostMapping("/usuarios/{email}/activar")
    public ResponseEntity<Map<String, String>> activarUsuario(@PathVariable String email) {
        servicioUsuarios.activarUsuario(email);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario activado"));
    }

    @PostMapping("/usuarios/{email}/cambiar-rol")
    public ResponseEntity<Map<String, String>> cambiarRolUsuario(@PathVariable String email,
                                                                 @RequestBody Map<String, String> body) {
        try {
            servicioUsuarios.cambiarRolUsuario(email, body.get("rol"));
            return ResponseEntity.ok(Map.of("mensaje", "Rol del usuario actualizado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
