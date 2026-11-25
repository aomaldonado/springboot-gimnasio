package com.puce.gimnasio.controller;

import com.puce.gimnasio.model.Socio;
import com.puce.gimnasio.service.SocioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socio")
public class SocioController {

    private SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    // LISTAR TODOS
    @GetMapping
    public List<Socio> verTodos() {
        return socioService.traerTodos();
    }

    // TRAER UN SOCIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Socio> traerUno(@PathVariable int id) {
        return socioService.traerUno(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GUARDAR
    @PostMapping
    public ResponseEntity<Socio> guardar(@RequestBody Socio socio) {
        Socio nuevoSocio = socioService.guardar(socio);
        return new ResponseEntity<>(nuevoSocio, HttpStatus.CREATED);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizar(@PathVariable int id, @RequestBody Socio socio) {
        return socioService.actualizar(id, socio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (socioService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // SUMA DE MENSUALIDADES DE SOCIOS ACTIVOS
    @GetMapping("/ingresos")
    public ResponseEntity<Double> obtenerIngresos() {
        return ResponseEntity.ok(socioService.calcularIngresos());
    }

    // MARCAR SOCIO COMO INACTIVO
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Socio> desactivar(@PathVariable int id) {
        return ResponseEntity.ok(socioService.desactivar(id));
    }
}
