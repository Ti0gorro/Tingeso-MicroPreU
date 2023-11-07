package com.tingeso.cuota.repository;

import com.tingeso.cuota.model.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    List<Cuota> findByRutEstudiante(String rutEstudiante);
}
