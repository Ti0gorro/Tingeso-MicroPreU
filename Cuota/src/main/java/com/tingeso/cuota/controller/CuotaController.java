package com.tingeso.cuota.controller;

import com.tingeso.cuota.model.Cuota;
import com.tingeso.cuota.service.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cuotas")
@CrossOrigin(origins = "http://localhost:3000")
public class CuotaController {

    @Autowired
    private CuotaService cuotaService;

    @GetMapping("/{rutEstudiante}")
    public ResponseEntity<List<Cuota>> listarCuotasEstudiante(@PathVariable String rutEstudiante) {
        List<Cuota> cuotas = cuotaService.obtenerCuotasPorRut(rutEstudiante);
        if (cuotas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cuotas);
    }

    @PostMapping("/pago/{idCuota}")
    public ResponseEntity<Cuota> pagarCuota(@PathVariable Long idCuota) {
        try {
            Cuota cuotaPagada = cuotaService.registrarPagoCuota(idCuota);
            return ResponseEntity.ok(cuotaPagada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarCuotasParaEstudiante(@RequestParam String rutEstudiante, @RequestParam int numeroCuotas) {
        try {
            List<Cuota> cuotasGeneradas = cuotaService.generarCuotas(rutEstudiante, numeroCuotas);
            return new ResponseEntity<>(cuotasGeneradas, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/montoTotalArancel/{rutEstudiante}")
    public ResponseEntity<Double> obtenerMontoTotalArancel(@PathVariable String rutEstudiante) {
        double montoTotalArancel = cuotaService.calcularMontoTotalArancel(rutEstudiante) - 70000;
        return ResponseEntity.ok(montoTotalArancel);
    }

    @GetMapping("/tipoPago/{rutEstudiante}")
    public ResponseEntity<String> obtenerTipoPago(@PathVariable String rutEstudiante) {
        String tipoPago = cuotaService.determinarTipoPago(rutEstudiante);
        return ResponseEntity.ok(tipoPago);
    }

    @GetMapping("/totalCuotasPactadas/{rutEstudiante}")
    public int obtenerTotalCuotasPactadas(@PathVariable String rutEstudiante) {
        return cuotaService.obtenerTotalCuotasPactadas(rutEstudiante);
    }

    @GetMapping("/cuotasPagadas/{rutEstudiante}")
    public int obtenerCuotasPagadas(@PathVariable String rutEstudiante) {
        return cuotaService.obtenerCuotasPagadas(rutEstudiante);
    }
    @GetMapping("/montoTotalPagado/{rutEstudiante}")
    public double obtenerMontoTotalPagado(@PathVariable String rutEstudiante) {
        return cuotaService.obtenerMontoTotalPagado(rutEstudiante);
    }
    @GetMapping("/saldoPorPagar/{rutEstudiante}")
    public ResponseEntity<Double> obtenerSaldoPorPagar(@PathVariable String rutEstudiante) {
        double saldoPorPagar = cuotaService.calcularMontoTotalArancel(rutEstudiante) - 70000 - cuotaService.obtenerMontoTotalPagado(rutEstudiante);
        return ResponseEntity.ok(saldoPorPagar);
    }
    @GetMapping("/cuotasConRetraso/{rutEstudiante}")
    public ResponseEntity<Integer> obtenerCuotasConRetraso(@PathVariable String rutEstudiante) {
        int cuotasConRetraso = cuotaService.obtenerCuotasConRetraso(rutEstudiante);
        return ResponseEntity.ok(cuotasConRetraso);
    }
    @GetMapping("/fechaUltimoPago/{rutEstudiante}")
    public ResponseEntity<Date> obtenerFechaUltimoPago(@PathVariable String rutEstudiante) {
        Date fechaUltimoPago = cuotaService.obtenerFechaUltimoPago(rutEstudiante);
        if (fechaUltimoPago != null) {
            return ResponseEntity.ok(fechaUltimoPago);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
