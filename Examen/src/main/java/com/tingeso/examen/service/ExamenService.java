package com.tingeso.examen.service;

import com.tingeso.examen.model.Examen;
import com.tingeso.examen.repository.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ExamenService {

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private RestTemplate restTemplate;

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
    public Map<String, Object> generarReporteEstadoPago(String rutEstudiante) {
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("rutEstudiante", rutEstudiante);
        reporte.put("nombreEstudiante", obtenerNombreEstudiante(rutEstudiante));
        reporte.put("numeroExamenesRendidos", obtenerCantidadExamenesPorRut(rutEstudiante));
        reporte.put("promedioPuntajeExamenes", obtenerPromedioEstudiante(rutEstudiante));
        reporte.put("montoTotalArancel", obtenerMontoTotalArancel(rutEstudiante));
        reporte.put("tipoPago", obtenerTipoPago(rutEstudiante));
        reporte.put("totalCuotasPactadas", obtenerTotalCuotasPactadas(rutEstudiante));
        reporte.put("cuotasPagadas", obtenerCuotasPagadas(rutEstudiante));
        reporte.put("MontoTotalPagado", obtenerMontoTotalPagado(rutEstudiante));
        reporte.put("saldoPorPagar", obtenerSaldoPorPagar(rutEstudiante));
        reporte.put("cuotasConRetraso", obtenerCuotasConRetraso(rutEstudiante));
        Date fechaUltimoPago = obtenerFechaUltimoPago(rutEstudiante);
        reporte.put("fechaUltimoPago", fechaUltimoPago != null ? fechaUltimoPago.toString() : "No disponible");
        return reporte;
    }
    private int obtenerTotalCuotasPactadas(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/totalCuotasPactadas/" + rutEstudiante;
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody();
        } catch (Exception e) {
            return -1;
        }
    }
    private int obtenerCuotasPagadas(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/cuotasPagadas/" + rutEstudiante;
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody();
        } catch (Exception e) {
            return -1;
        }
    }
    private String obtenerNombreEstudiante(String rutEstudiante) {
        String url = "http://localhost:8080/api/estudiantes/nombreCompleto/" + rutEstudiante;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "No disponible";
        }
    }
    public int obtenerCantidadExamenesPorRut(String rutEstudiante) {
        List<Examen> examenes = examenRepository.findByRutEstudiante(rutEstudiante);
        return examenes.size();
    }
    public double obtenerPromedioEstudiante(String rutEstudiante) {
        List<Examen> examenes = examenRepository.findByRutEstudiante(rutEstudiante);
        if (examenes.isEmpty()) {
            return 0.0;
        }
        int sumaPuntajes = examenes.stream().mapToInt(Examen::getPuntaje).sum();
        return (double) sumaPuntajes / examenes.size();
    }
    public double obtenerMontoTotalArancel(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/montoTotalArancel/" + rutEstudiante;
        try {
            ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
            return response.getBody() != null ? response.getBody() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
    public String obtenerTipoPago(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/tipoPago/" + rutEstudiante;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String tipoPago = response.getBody();
            return tipoPago != null ? tipoPago : "Desconocido";
        } catch (Exception e) {
            return "Desconocido";
        }
    }
    private double obtenerMontoTotalPagado(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/montoTotalPagado/" + rutEstudiante;
        try {
            ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
            return response.getBody();
        } catch (Exception e) {
            return 0;
        }
    }
    private double obtenerSaldoPorPagar(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/saldoPorPagar/" + rutEstudiante;
        try {
            ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
            return response.getBody() != null ? response.getBody() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
    private int obtenerCuotasConRetraso(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/cuotasConRetraso/" + rutEstudiante;
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody() != null ? response.getBody() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
    private Date obtenerFechaUltimoPago(String rutEstudiante) {
        String url = "http://localhost:8081/api/cuotas/fechaUltimoPago/" + rutEstudiante;
        try {
            ResponseEntity<Date> response = restTemplate.getForEntity(url, Date.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
