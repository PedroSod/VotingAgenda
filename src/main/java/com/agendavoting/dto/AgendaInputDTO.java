package com.agendavoting.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AgendaInputDTO(
        @NotBlank String title,
        String description
) implements Serializable {
}
