package com.tingeso.examen.service;

import com.tingeso.examen.model.Examen;
import com.tingeso.examen.repository.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExamenService {

    @Autowired
    private ExamenRepository examenRepository;

    public void importarExamenes(MultipartFile file) throws Exception {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
            throw new Exception("Formato de archivo no válido. Por favor, suba un archivo CSV.");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Examen> examenes = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 3) {
                    throw new Exception("Formato de archivo CSV incorrecto");
                }
                Examen examen = new Examen();
                examen.setRutEstudiante(fields[0]);
                examen.setFecha(new SimpleDateFormat("dd/MM/yyyy").parse(fields[1]));
                examen.setPuntaje(Integer.parseInt(fields[2]));
                examenes.add(examen);
            }
            examenRepository.saveAll(examenes);
        } catch (Exception e) {
            throw new Exception("Error al procesar el archivo CSV: " + e.getMessage());
        }
    }
}
