package com.puce.gimnasio.controller;

import com.puce.gimnasio.model.Maquina;
import com.puce.gimnasio.service.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maquina")
public class MaquinaController {

    private MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    // LISTAR TODOS
    @GetMapping
    public List<Maquina> verTodos() {
        return maquinaService.traerTodos();
    }

    // TRAER UNA MAQUINA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Maquina> traerUno(@PathVariable int id) {
        return maquinaService.traerUno(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GUARDAR
    @PostMapping
    public ResponseEntity<Maquina> guardar(@RequestBody Maquina maquina) {
        Maquina nuevaMaquina = maquinaService.guardar(maquina);
        return new ResponseEntity<>(nuevaMaquina, HttpStatus.CREATED);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Maquina> actualizar(@PathVariable int id, @RequestBody Maquina maquina) {
        return maquinaService.actualizar(id, maquina)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (maquinaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // MÁQUINAS QUE NO ESTÁN EN MANTENIMIENTO
    @GetMapping("/operativas")
    public ResponseEntity<List<Maquina>> traerOperativas() {
        return ResponseEntity.ok(maquinaService.traerOperativas());
    }

    // MÁQUINAS CON MÁS DE 'anios' AÑOS DESDE SU COMPRA
    @GetMapping("/antiguas")
    public ResponseEntity<List<Maquina>> traerAntiguas(@RequestParam(defaultValue = "5") int anios) {
        return ResponseEntity.ok(maquinaService.traerAntiguas(anios));
    }

    // ENVÍA LA MÁQUINA A MANTENIMIENTO
    @PutMapping("/{id}/mantenimiento")
    public ResponseEntity<Maquina> enviarAMantenimiento(@PathVariable int id) {
        return ResponseEntity.ok(maquinaService.enviarAMantenimiento(id));
    }
}
