package com.tingeso.examen.controller;

import com.tingeso.examen.service.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/examenes")
@CrossOrigin(origins = "http://localhost:3000")
public class ExamenController {

    @Autowired
    private ExamenService examenService;
    @PostMapping("/importar")
    public ResponseEntity<String> importarExamenes(@RequestParam("file") MultipartFile file) {
        try {
            examenService.importarExamenes(file);
            return ResponseEntity.ok("Archivo importado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al importar el archivo: " + e.getMessage());
        }
    }
    @GetMapping("/reporte/{rut}")
    public ResponseEntity<Map<String, Object>> obtenerReportePorRut(@PathVariable String rut) {
        try {
            Map<String, Object> reporte = examenService.generarReporteEstadoPago(rut);
            if (reporte.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
