package com.forohub.repository;

import com.forohub.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    // Metodo para buscar un curso por su nombre
    Optional<Curso> findByNombre(String nombre);
}