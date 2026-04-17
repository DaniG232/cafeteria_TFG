package com.dani.cafeteria.service.impl;

import com.dani.cafeteria.entity.Plato;
import com.dani.cafeteria.repository.PlatoRepository;
import com.dani.cafeteria.service.IServicioPlatos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioPlatosImpl implements IServicioPlatos {

    private final PlatoRepository platoRepository;

    public ServicioPlatosImpl(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    @Override
    public Plato crearPlato(Plato plato) {
        plato.setActivo(true);
        return platoRepository.save(plato);
    }

    @Override
    public Plato buscarPorId(Integer id) {
        return platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado: " + id));
    }

    @Override
    public List<Plato> listarPlatos() {
        return platoRepository.findAll();
    }

    @Override
    public List<Plato> listarPlatosActivos() {
        return platoRepository.findByActivoTrue();
    }

    @Override
    public List<Plato> listarPorTipo(Plato.TipoPlato tipo) {
        return platoRepository.findByTipo(tipo);
    }

    @Override
    public Plato actualizarPlato(Integer id, Plato plato) {
        Plato existente = buscarPorId(id);
        existente.setNombre(plato.getNombre());
        existente.setDescripcion(plato.getDescripcion());
        existente.setTipo(plato.getTipo());
        existente.setPrecio(plato.getPrecio());
        return platoRepository.save(existente);
    }

    @Override
    public void desactivarPlato(Integer id) {
        Plato plato = buscarPorId(id);
        plato.setActivo(false);
        platoRepository.save(plato);
    }
}
