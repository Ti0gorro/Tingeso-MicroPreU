package com.tingeso.estudiante.repositorio;

import com.tingeso.estudiante.entidad.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepositorio extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByRut(String rut);
}
