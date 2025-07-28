package com.forohub.dto;

import com.forohub.model.Topico;
//import com.forohub.model.StatusTopico; // Asegúrate de crear este ENUM si decides usarlo

import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String status, // O StatusTopico status si usas el ENUM
        String autorNombre,
        String cursoNombre
) {
    public DatosListadoTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(), // O topico.getStatus().toString() si usas el ENUM
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre()
        );
    }
}