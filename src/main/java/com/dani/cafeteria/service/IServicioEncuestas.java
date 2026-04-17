package com.dani.cafeteria.service;

import com.dani.cafeteria.entity.Encuesta;
import com.dani.cafeteria.entity.Rellena;

import java.util.List;

public interface IServicioEncuestas {

    Encuesta crearEncuesta(Encuesta encuesta);

    Encuesta buscarPorId(Integer id);

    List<Encuesta> listarEncuestas();

    void rellenarEncuesta(String emailUsuario, Integer idEncuesta);

    boolean haRellenado(String emailUsuario, Integer idEncuesta);

    List<Rellena> listarRespuestasEncuesta(Integer idEncuesta);

    long contarRespuestasEncuesta(Integer idEncuesta);

    void eliminarEncuesta(Integer id);
}
