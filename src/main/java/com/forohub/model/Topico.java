package com.forohub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion; // Corregido a camelCase para Java
    private String status; // Podría ser un ENUM más adelante, por ahora String

    @ManyToOne(fetch = FetchType.LAZY) // Lazy por defecto para ManyToOne
    @JoinColumn(name = "autor_id") // Nombre de la columna de la clave foránea en la tabla topicos
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id") // Nombre de la columna de la clave foránea en la tabla topicos
    private Curso curso;

    // Puedes añadir un constructor para cuando se crea un nuevo tópico
    public Topico(String titulo, String mensaje, Usuario autor, Curso curso) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDateTime.now(); // Establece la fecha actual al crear
        this.status = "ACTIVO"; // Estado inicial por defecto
        this.autor = autor;
        this.curso = curso;
    }
}