package com.tingeso.examen.controller;

import com.tingeso.examen.service.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
