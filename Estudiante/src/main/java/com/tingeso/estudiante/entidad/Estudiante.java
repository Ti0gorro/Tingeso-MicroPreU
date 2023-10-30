package com.tingeso.estudiante.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@NoArgsConstructor
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private Date fechaNacimiento;

    @Column(nullable = false)
    private Integer tipoColegioProcedencia;

    @Column(nullable = false)
    private String nombreColegio;

    @Column(nullable = false)
    private int anoEgresoColegio;

}

