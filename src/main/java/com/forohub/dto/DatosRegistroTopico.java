package com.forohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank // No puede ser null ni vacío ni solo espacios en blanco
        String titulo,
        @NotBlank
        String mensaje,
        @NotNull // No puede ser null (el ID del autor es un Long)
        Long idAutor,
        @NotNull
        Long idCurso
) {}