package com.forohub.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DatosActualizacionTopico(
        @NotNull
        Long id,
        @Size(max = 255)
        String titulo,
        String mensaje,
        String status
) {
}