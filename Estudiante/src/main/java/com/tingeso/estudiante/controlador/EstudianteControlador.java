package com.tingeso.estudiante.controlador;

import com.tingeso.estudiante.entidad.Estudiante;
import com.tingeso.estudiante.servicio.EstudianteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/tipoColegio/{rut}")
    public ResponseEntity<Integer> obtenerTipoColegioProcedencia(@PathVariable String rut) {
        return estudianteService.obtenerTipoColegioProcedenciaPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/anosEgreso/{rut}")
    public ResponseEntity<Integer> obtenerAnosDesdeEgreso(@PathVariable String rut) {
        return estudianteService.obtenerAnosDesdeEgreso(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombreCompleto/{rut}")
    public ResponseEntity<String> obtenerNombreCompletoEstudiantePorRut(@PathVariable String rut) {
        return estudianteService.obtenerEstudiantePorRut(rut)
                .map(estudiante -> ResponseEntity.ok(estudiante.getNombres() + " " + estudiante.getApellidos()))
                .orElse(ResponseEntity.notFound().build());
    }

}
