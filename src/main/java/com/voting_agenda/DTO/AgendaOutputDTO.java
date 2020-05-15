package com.voting_agenda.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@JsonPropertyOrder({"id", "title", "description"})

public class AgendaOutputDTO extends AgendaInputDTO implements Serializable {

    private static final long serialVersionUID = 3184317326177799715L;
    private String id;
}
