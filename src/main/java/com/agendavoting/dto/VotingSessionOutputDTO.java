package com.agendavoting.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record VotingSessionOutputDTO(
        String id,
        AgendaOutputDTO agenda,
        LocalDateTime start,
        LocalDateTime end
) implements Serializable {
}
