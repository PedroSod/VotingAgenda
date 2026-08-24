package com.agendavoting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.time.LocalDateTime;

public record VotingSessionInputDTO(
        @NotBlank String agendaId,
        LocalDateTime start,
        @Positive Long timeDuration
) implements Serializable {
}
