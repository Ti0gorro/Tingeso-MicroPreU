package com.tingeso.estudiante.controlador;

import com.tingeso.estudiante.entidad.Estudiante;
import com.tingeso.estudiante.servicio.EstudianteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "http://localhost:3000")
public class EstudianteControlador {

    @Autowired
    private EstudianteServicio estudianteService;

    @GetMapping
    public ResponseEntity<List<Estudiante>> obtenerTodosLosEstudiantes() {
        return ResponseEntity.ok(estudianteService.obtenerTodosLosEstudiantes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerEstudiantePorId(@PathVariable Long id) {
        return estudianteService.obtenerEstudiantePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        return ResponseEntity.ok(estudianteService.guardarEstudiante(estudiante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizarEstudiante(@PathVariable Long id, @RequestBody Estudiante estudiante) {
        if (!estudianteService.existeEstudiantePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        estudiante.setId(id);
        return ResponseEntity.ok(estudianteService.guardarEstudiante(estudiante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        if (!estudianteService.existeEstudiantePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        estudianteService.eliminarEstudiante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipoColegioProcedencia")
    public ResponseEntity<Integer> obtenerTipoColegioProcedenciaPorRut(@RequestParam String rut) {
        Optional<Estudiante> estudiante = estudianteService.obtenerEstudiantePorRut(rut);
        if (estudiante != null) {
            return ResponseEntity.ok(estudiante.getTipoColegioProcedencia());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
