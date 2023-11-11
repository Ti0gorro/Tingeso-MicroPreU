package com.tingeso.examen.repository;

import com.tingeso.examen.model.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {
    List<Examen> findByRutEstudiante(String rutEstudiante);
}
