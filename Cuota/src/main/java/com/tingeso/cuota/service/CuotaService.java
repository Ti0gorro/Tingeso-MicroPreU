package com.tingeso.cuota.service;

import com.tingeso.cuota.model.Cuota;
import com.tingeso.cuota.repository.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            return cuotaRepository.save(cuota);
        } else {
            throw new IllegalStateException("La cuota ya está pagada o en estado no apto para pago.");
        }
    }

    public List<Cuota> generarCuotas(String rutEstudiante, int numeroCuotasSolicitadas) {
        int tipoColegioProcedencia = obtenerTipoColegioProcedencia(rutEstudiante);
        double montoMatricula = 70000;
        double montoArancel = 1500000;
        double montoFinalArancel;

        if (numeroCuotasSolicitadas == 1) {
            montoFinalArancel = montoArancel * 0.5;
        } else {
            double descuentoPorTipoColegio = obtenerDescuentoPorTipoColegio(tipoColegioProcedencia);
            montoFinalArancel = montoArancel * (1 - descuentoPorTipoColegio);

        int numeroMaxCuotas = obtenerNumeroMaxCuotasPorTipoColegio(tipoColegioProcedencia);
        int numeroCuotas = numeroCuotasSolicitadas > 1 ? Math.min(numeroCuotasSolicitadas, numeroMaxCuotas) : 1;
        double montoCuota = montoFinalArancel / numeroCuotas;

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

    private int obtenerTipoColegioProcedencia(String rutEstudiante) {
        // TERMINAR
        return 1;
    }

    private double obtenerDescuentoPorTipoColegio(int tipoColegio) {
        switch (tipoColegio) {
            case 1: return 0.20; 
            case 2: return 0.10; 
            case 3: return 0.0;  
        }
    }

    private int obtenerNumeroMaxCuotasPorTipoColegio(int tipoColegio) {
        switch (tipoColegio) {
            case 1: return 10; 
            case 2: return 7;  
            case 3: return 4;  
            default: return 0; 
        }
    }

}
