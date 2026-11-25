package com.puce.gimnasio.service;

import com.puce.gimnasio.model.ClaseGrupal;
import com.puce.gimnasio.repository.ClaseGrupalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ClaseGrupalService {

    private ClaseGrupalRepository claseGrupalRepository;

    public ClaseGrupalService(ClaseGrupalRepository claseGrupalRepository) {
        this.claseGrupalRepository = claseGrupalRepository;
    }

    // TRAER TODOS
    public List<ClaseGrupal> traerTodos() {
        return claseGrupalRepository.findAll();
    }

    // TRAER UNO
    public Optional<ClaseGrupal> traerUno(int id) {
        return claseGrupalRepository.findById(id);
    }

    // GUARDAR
    public ClaseGrupal guardar(ClaseGrupal claseGrupal) {
        return claseGrupalRepository.save(claseGrupal);
    }

    // ACTUALIZAR
    public Optional<ClaseGrupal> actualizar(int id, ClaseGrupal claseActualizada) {
        return claseGrupalRepository.findById(id).map(clase -> {
            clase.setNombre(claseActualizada.getNombre());
            clase.setInstructor(claseActualizada.getInstructor());
            clase.setHorario(claseActualizada.getHorario());
            clase.setCupos(claseActualizada.getCupos());
            return claseGrupalRepository.save(clase);
        });
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        if (claseGrupalRepository.existsById(id)) {
            claseGrupalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // INSCRIBIR: RESTA UN CUPO (200, 404, 409 si no quedan cupos)
    public ClaseGrupal inscribir(int id) {
        ClaseGrupal clase = claseGrupalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase grupal no encontrada"));
        if (clase.getCupos() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No quedan cupos disponibles");
        }
        clase.setCupos(clase.getCupos() - 1);
        return claseGrupalRepository.save(clase);
    }

    // CANCELAR INSCRIPCIÓN: SUMA UN CUPO (200, 404)
    public ClaseGrupal cancelarInscripcion(int id) {
        ClaseGrupal clase = claseGrupalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase grupal no encontrada"));
        clase.setCupos(clase.getCupos() + 1);
        return claseGrupalRepository.save(clase);
    }

    // CLASES CON AL MENOS UN CUPO, DE MÁS A MENOS CUPOS
    public List<ClaseGrupal> traerConCupos() {
        return claseGrupalRepository.findByCuposGreaterThanOrderByCuposDesc(0);
    }
}
