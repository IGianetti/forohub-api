package com.forohub.repository;

import com.forohub.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);


    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Topico t WHERE t.titulo = :titulo AND t.mensaje = :mensaje AND t.id <> :idExcluido")
    boolean existsByTituloAndMensajeAndIdIsNot(String titulo, String mensaje, Long idExcluido);

    Page<Topico> findByCursoNombreContainingIgnoreCase(String nombreCurso, Pageable paginacion);

    @Query("SELECT t FROM Topico t WHERE YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByFechaCreacionYear(int anio, Pageable paginacion);

    @Query("SELECT t FROM Topico t WHERE t.curso.nombre LIKE %:nombreCurso% AND YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByCursoNombreContainingIgnoreCaseAndFechaCreacionYear(String nombreCurso, int anio, Pageable paginacion);
}