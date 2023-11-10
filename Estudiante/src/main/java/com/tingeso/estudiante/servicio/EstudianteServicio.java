package com.tingeso.estudiante.servicio;

import com.tingeso.estudiante.entidad.Estudiante;
import com.tingeso.estudiante.repositorio.EstudianteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteServicio {

    @Autowired
    private EstudianteRepositorio estudianteRepositorio;

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteRepositorio.findAll();
    }

    public Estudiante guardarEstudiante(Estudiante estudiante) {
        return estudianteRepositorio.save(estudiante);
    }

    public Optional<Estudiante> obtenerEstudiantePorId(Long id) {
        return estudianteRepositorio.findById(id);
    }

    public void eliminarEstudiante(Long id) {
        estudianteRepositorio.deleteById(id);
    }

    public boolean existeEstudiantePorId(Long id) {
        return estudianteRepositorio.existsById(id);
    }

    public Optional<Estudiante> obtenerEstudiantePorRut(String rut) {
        return estudianteRepositorio.findByRut(rut);
    }
}
