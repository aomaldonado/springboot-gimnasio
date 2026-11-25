package com.puce.gimnasio.service;

import com.puce.gimnasio.model.Socio;
import com.puce.gimnasio.repository.SocioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SocioService {

    private SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    // TRAER TODOS
    public List<Socio> traerTodos() {
        return socioRepository.findAll();
    }

    // TRAER UNO
    public Optional<Socio> traerUno(int id) {
        return socioRepository.findById(id);
    }

    // GUARDAR
    public Socio guardar(Socio socio) {
        return socioRepository.save(socio);
    }

    // ACTUALIZAR
    public Optional<Socio> actualizar(int id, Socio socioActualizado) {
        return socioRepository.findById(id).map(socio -> {
            socio.setNombre(socioActualizado.getNombre());
            socio.setPlan(socioActualizado.getPlan());
            socio.setMensualidad(socioActualizado.getMensualidad());
            socio.setActivo(socioActualizado.isActivo());
            return socioRepository.save(socio);
        });
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        if (socioRepository.existsById(id)) {
            socioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // SUMA DE MENSUALIDADES DE SOCIOS ACTIVOS
    public double calcularIngresos() {
        return socioRepository.findByActivoTrue()
                .stream()
                .mapToDouble(Socio::getMensualidad)
                .sum();
    }

    // MARCAR SOCIO COMO INACTIVO (200, 404, 409)
    public Socio desactivar(int id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));
        if (!socio.isActivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El socio ya está inactivo");
        }
        socio.setActivo(false);
        return socioRepository.save(socio);
    }
}
