package com.dani.cafeteria.service.impl;

import com.dani.cafeteria.entity.Encuesta;
import com.dani.cafeteria.entity.Rellena;
import com.dani.cafeteria.entity.Usuario;
import com.dani.cafeteria.repository.EncuestaRepository;
import com.dani.cafeteria.repository.RellenaRepository;
import com.dani.cafeteria.service.IServicioEncuestas;
import com.dani.cafeteria.service.IServicioUsuarios;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioEncuestasImpl implements IServicioEncuestas {

    private final EncuestaRepository encuestaRepository;
    private final RellenaRepository rellenaRepository;
    private final IServicioUsuarios servicioUsuarios;

    public ServicioEncuestasImpl(EncuestaRepository encuestaRepository,
                                  RellenaRepository rellenaRepository,
                                  IServicioUsuarios servicioUsuarios) {
        this.encuestaRepository = encuestaRepository;
        this.rellenaRepository = rellenaRepository;
        this.servicioUsuarios = servicioUsuarios;
    }

    @Override
    public Encuesta crearEncuesta(Encuesta encuesta) {
        encuesta.setFechaCreacion(LocalDate.now());
        return encuestaRepository.save(encuesta);
    }

    @Override
    public Encuesta buscarPorId(Integer id) {
        return encuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada: " + id));
    }

    @Override
    public List<Encuesta> listarEncuestas() {
        return encuestaRepository.findAllByOrderByFechaCreacionDesc();
    }

    @Override
    public void rellenarEncuesta(String emailUsuario, Integer idEncuesta) {
        if (haRellenado(emailUsuario, idEncuesta)) {
            throw new RuntimeException("Ya has rellenado esta encuesta");
        }

        Usuario usuario = servicioUsuarios.buscarPorEmail(emailUsuario);
        Encuesta encuesta = buscarPorId(idEncuesta);

        Rellena rellena = Rellena.builder()
                .usuario(usuario)
                .encuesta(encuesta)
                .fechaRespuesta(LocalDate.now())
                .build();

        rellenaRepository.save(rellena);
    }

    @Override
    public boolean haRellenado(String emailUsuario, Integer idEncuesta) {
        return rellenaRepository
                .findByUsuarioEmailAndEncuestaIdEncuesta(emailUsuario, idEncuesta)
                .isPresent();
    }

    @Override
    public List<Rellena> listarRespuestasEncuesta(Integer idEncuesta) {
        return rellenaRepository.findByEncuestaIdEncuesta(idEncuesta);
    }

    @Override
    public long contarRespuestasEncuesta(Integer idEncuesta) {
        return rellenaRepository.findByEncuestaIdEncuesta(idEncuesta).size();
    }

    @Override
    public void eliminarEncuesta(Integer id) {
        Encuesta encuesta = buscarPorId(id);
        encuestaRepository.delete(encuesta);
    }
}
