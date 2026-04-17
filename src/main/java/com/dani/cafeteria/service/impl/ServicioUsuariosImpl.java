package com.dani.cafeteria.service.impl;

import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.repository.UsuarioRepository;
import com.dani.cafeteria.service.IServicioUsuarios;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioUsuariosImpl implements IServicioUsuarios {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ServicioUsuariosImpl(UsuarioRepository usuarioRepository,
                                BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.existsById(usuario.getEmail())) {
            throw new RuntimeException("El usuario con email " + usuario.getEmail() + " ya existe");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setEstado(true);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByEstadoTrue();
    }

    @Override
    public void desactivarUsuario(String email) {
        Usuario usuario = buscarPorEmail(email);
        usuario.setEstado(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void activarUsuario(String email) {
        Usuario usuario = buscarPorEmail(email);
        usuario.setEstado(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public boolean existeUsuario(String email) {
        return usuarioRepository.existsById(email);
    }
}
