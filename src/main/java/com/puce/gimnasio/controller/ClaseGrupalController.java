package com.puce.gimnasio.controller;

import com.puce.gimnasio.model.ClaseGrupal;
import com.puce.gimnasio.service.ClaseGrupalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clase")
public class ClaseGrupalController {

    private ClaseGrupalService claseGrupalService;

    public ClaseGrupalController(ClaseGrupalService claseGrupalService) {
        this.claseGrupalService = claseGrupalService;
    }

    // LISTAR TODOS
    @GetMapping
    public List<ClaseGrupal> verTodos() {
        return claseGrupalService.traerTodos();
    }

    // TRAER UNA CLASE GRUPAL POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ClaseGrupal> traerUno(@PathVariable int id) {
        return claseGrupalService.traerUno(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GUARDAR
    @PostMapping
    public ResponseEntity<ClaseGrupal> guardar(@RequestBody ClaseGrupal claseGrupal) {
        ClaseGrupal nuevaClaseGrupal = claseGrupalService.guardar(claseGrupal);
        return new ResponseEntity<>(nuevaClaseGrupal, HttpStatus.CREATED);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<ClaseGrupal> actualizar(@PathVariable int id, @RequestBody ClaseGrupal claseGrupal) {
        return claseGrupalService.actualizar(id, claseGrupal)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (claseGrupalService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // INSCRIBIR (RESTA UN CUPO)
    @PutMapping("/{id}/inscribir")
    public ResponseEntity<ClaseGrupal> inscribir(@PathVariable int id) {
        return ResponseEntity.ok(claseGrupalService.inscribir(id));
    }

    // CANCELAR INSCRIPCIÓN (SUMA UN CUPO)
    @PutMapping("/{id}/cancelar-inscripcion")
    public ResponseEntity<ClaseGrupal> cancelarInscripcion(@PathVariable int id) {
        return ResponseEntity.ok(claseGrupalService.cancelarInscripcion(id));
    }

    // CLASES CON AL MENOS UN CUPO, DE MÁS A MENOS CUPOS
    @GetMapping("/con-cupos")
    public ResponseEntity<List<ClaseGrupal>> traerConCupos() {
        return ResponseEntity.ok(claseGrupalService.traerConCupos());
    }
}
