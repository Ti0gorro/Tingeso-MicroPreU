package com.tingeso.cuota.service;

import com.tingeso.cuota.model.Cuota;
import com.tingeso.cuota.repository.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuotaService {

    @Autowired
    private CuotaRepository cuotaRepository;
    @Autowired
    private RestTemplate restTemplate;

    public List<Cuota> obtenerCuotasPorRut(String rutEstudiante) {
        return cuotaRepository.findByRutEstudiante(rutEstudiante);
    }

    public Cuota registrarPagoCuota(Long idCuota) {
        Cuota cuota = cuotaRepository.findById(idCuota)
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada"));
        if (cuota.getEstado().equals(Cuota.EstadoCuota.PENDIENTE) || cuota.getEstado().equals(Cuota.EstadoCuota.VENCIDA)) {
            cuota.setEstado(Cuota.EstadoCuota.PAGADA);
            cuota.setFechaDePago(Date.valueOf(LocalDate.now()));
            return cuotaRepository.save(cuota);
        } else {
            throw new IllegalStateException("La cuota ya está pagada o en estado no apto para pago.");
        }
    }

    public int obtenerTipoColegioProcedencia(String rutEstudiante) {
        String url = "http://localhost:8080/api/estudiantes/tipoColegio/" + rutEstudiante;
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody();
        } catch (Exception e) {
            return -1;
        }
    }
    private double obtenerDescuentoPorTipoColegio(int tipoColegio) {
        switch (tipoColegio) {
            case 1: return 0.20;
            case 2: return 0.10;
            case 3: return 0.0;
        }
        return 0;
    }
    private int obtenerNumeroMaxCuotasPorTipoColegio(int tipoColegio) {
        switch (tipoColegio) {
            case 1: return 10;
            case 2: return 7;
            case 3: return 4;
            default: return 0;
        }
    }
    private double obtenerDescuentoPorAnosEgreso(int anosDesdeEgreso) {
        if (anosDesdeEgreso < 1) {
            return 0.15;
        } else if (anosDesdeEgreso <= 2) {
            return 0.08;
        } else if (anosDesdeEgreso <= 4) {
            return 0.04;
        } else {
            return 0.0;
        }
    }
    private int obtenerAnosDesdeEgreso(String rutEstudiante) {
        String url = "http://localhost:8080/api/estudiantes/anosEgreso/" + rutEstudiante;
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody() != null ? response.getBody() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
    public List<Cuota> generarCuotas(String rutEstudiante, int numeroCuotasSolicitadas) {
        int tipoColegioProcedencia = obtenerTipoColegioProcedencia(rutEstudiante);
        int anosDesdeEgreso = obtenerAnosDesdeEgreso(rutEstudiante);
        double descuentoPorEgreso = obtenerDescuentoPorAnosEgreso(anosDesdeEgreso);

        double montoMatricula = 70000;
        double montoArancel = 1500000;
        double montoFinalArancel;

        if (numeroCuotasSolicitadas == 1) {
            montoFinalArancel = montoArancel * 0.5;
        } else {
            double descuentoPorTipoColegio = obtenerDescuentoPorTipoColegio(tipoColegioProcedencia);
            montoFinalArancel = montoArancel * (1 - descuentoPorTipoColegio - descuentoPorEgreso);
        }
        int numeroMaxCuotas = obtenerNumeroMaxCuotasPorTipoColegio(tipoColegioProcedencia);
        int numeroCuotas = numeroCuotasSolicitadas > 1 ? Math.min(numeroCuotasSolicitadas, numeroMaxCuotas) : 1;
        double montoCuota = Math.round(montoFinalArancel / numeroCuotas);

        List<Cuota> cuotasGeneradas = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        Date fechaVencimientoMatricula = Date.valueOf(fechaActual);
        Date fechaVencimientoArancel = Date.valueOf(fechaActual.plusMonths(1).withDayOfMonth(10));
        Cuota cuotaMatricula = new Cuota();
        cuotaMatricula.setRutEstudiante(rutEstudiante);
        cuotaMatricula.setNumeroDeCuota(0);
        cuotaMatricula.setMonto(montoMatricula);
        cuotaMatricula.setFechaDeVencimiento(fechaVencimientoMatricula);
        cuotaMatricula.setEstado(Cuota.EstadoCuota.PENDIENTE);
        cuotasGeneradas.add(cuotaMatricula);
        for (int i = 1; i <= numeroCuotas; i++) {
            Cuota cuota = new Cuota();
            cuota.setRutEstudiante(rutEstudiante);
            cuota.setNumeroDeCuota(i);
            cuota.setMonto(montoCuota);
            cuota.setFechaDeVencimiento(fechaVencimientoArancel);
            cuota.setEstado(Cuota.EstadoCuota.PENDIENTE);
            cuotasGeneradas.add(cuota);
            fechaVencimientoArancel = Date.valueOf(fechaVencimientoArancel.toLocalDate().plusMonths(1).withDayOfMonth(10));
        }
        return cuotaRepository.saveAll(cuotasGeneradas);
    }
    public double calcularMontoTotalArancel(String rutEstudiante) {
        List<Cuota> cuotas = cuotaRepository.findByRutEstudiante(rutEstudiante);
        return cuotas.stream()
                .mapToDouble(Cuota::getMonto)
                .sum();
    }
    public String determinarTipoPago(String rutEstudiante) {
        List<Cuota> cuotas = cuotaRepository.findByRutEstudiante(rutEstudiante);
        int numeroCuotas = cuotas.size();
        return numeroCuotas > 2 ? "Cuotas" : "Contado";
    }
    public int obtenerTotalCuotasPactadas(String rutEstudiante) {
        List<Cuota> cuotas = cuotaRepository.findByRutEstudiante(rutEstudiante);
        return cuotas.size()-1;
    }

    public int obtenerCuotasPagadas(String rutEstudiante) {
        return (int) cuotaRepository.findByRutEstudiante(rutEstudiante).stream()
                .filter(cuota -> cuota.getEstado().equals(Cuota.EstadoCuota.PAGADA) && cuota.getNumeroDeCuota() != 0)
                .count();
    }
    public double obtenerMontoTotalPagado(String rutEstudiante) {
        return cuotaRepository.findByRutEstudiante(rutEstudiante).stream()
                .filter(cuota -> cuota.getEstado().equals(Cuota.EstadoCuota.PAGADA) && cuota.getNumeroDeCuota() != 0)
                .mapToDouble(Cuota::getMonto)
                .sum();
    }
    public int obtenerCuotasConRetraso(String rutEstudiante) {
        return (int) cuotaRepository.findByRutEstudiante(rutEstudiante).stream()
                .filter(cuota -> cuota.getEstado().equals(Cuota.EstadoCuota.VENCIDA))
                .count();
    }
    public Date obtenerFechaUltimoPago(String rutEstudiante) {
        List<Cuota> cuotasPagadas = cuotaRepository.findByRutEstudiante(rutEstudiante).stream()
                .filter(cuota -> cuota.getEstado().equals(Cuota.EstadoCuota.PAGADA))
                .collect(Collectors.toList());

        return cuotasPagadas.stream()
                .max(Comparator.comparing(Cuota::getFechaDePago))
                .map(Cuota::getFechaDePago)
                .orElse(null);
    }
}
