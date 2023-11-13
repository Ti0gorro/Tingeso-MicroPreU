package com.tingeso.cuota.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "cuotas")
@Getter
@Setter
@NoArgsConstructor
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numeroDeCuota;

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private Date fechaDeVencimiento;
    @Column(nullable = true)
    private Date fechaDePago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuota estado;

    @Column(nullable = false)
    private String rutEstudiante;

    public enum EstadoCuota {
        PAGADA,
        PENDIENTE,
        VENCIDA,
        REEMBOLSADO
    }
}
