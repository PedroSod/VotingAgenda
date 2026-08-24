package com.agendavoting.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({"id", "title", "description"})
public record AgendaOutputDTO(
        String id,
        String title,
        String description
) implements Serializable {
}
