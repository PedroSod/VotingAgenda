package com.votingAgenda.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id", "title", "description"})
public class AgendaOutputDTO extends AgendaInputDTO implements Serializable {

    private static final long serialVersionUID = 3184317326177799715L;
    private String id;
}
