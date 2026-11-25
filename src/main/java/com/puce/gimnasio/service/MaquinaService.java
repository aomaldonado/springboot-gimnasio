package com.puce.gimnasio.service;

import com.puce.gimnasio.model.Maquina;
import com.puce.gimnasio.repository.MaquinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class MaquinaService {

    private MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    // TRAER TODOS
    public List<Maquina> traerTodos() {
        return maquinaRepository.findAll();
    }

    // TRAER UNO
    public Optional<Maquina> traerUno(int id) {
        return maquinaRepository.findById(id);
    }

    // GUARDAR
    public Maquina guardar(Maquina maquina) {
        return maquinaRepository.save(maquina);
    }

    // ACTUALIZAR
    public Optional<Maquina> actualizar(int id, Maquina maquinaActualizada) {
        return maquinaRepository.findById(id).map(maquina -> {
            maquina.setNombre(maquinaActualizada.getNombre());
            maquina.setMarca(maquinaActualizada.getMarca());
            maquina.setAnioCompra(maquinaActualizada.getAnioCompra());
            maquina.setEnMantenimiento(maquinaActualizada.isEnMantenimiento());
            return maquinaRepository.save(maquina);
        });
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        if (maquinaRepository.existsById(id)) {
            maquinaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // MÁQUINAS QUE NO ESTÁN EN MANTENIMIENTO
    public List<Maquina> traerOperativas() {
        return maquinaRepository.findByEnMantenimientoFalse();
    }

    // MÁQUINAS CON MÁS DE 'anios' AÑOS DESDE SU COMPRA (USA EL AÑO ACTUAL)
    public List<Maquina> traerAntiguas(int anios) {
        int anioLimite = Year.now().getValue() - anios;
        return maquinaRepository.findByAnioCompraLessThan(anioLimite);
    }

    // ENVÍA LA MÁQUINA A MANTENIMIENTO (200, 404, 409 si ya estaba)
    public Maquina enviarAMantenimiento(int id) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Máquina no encontrada"));
        if (maquina.isEnMantenimiento()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La máquina ya se encuentra en mantenimiento");
        }
        maquina.setEnMantenimiento(true);
        return maquinaRepository.save(maquina);
    }
}
