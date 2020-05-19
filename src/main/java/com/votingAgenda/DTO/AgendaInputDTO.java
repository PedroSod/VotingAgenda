package com.votingAgenda.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class AgendaInputDTO implements Serializable {

	private static final long serialVersionUID = 3184317326177799715L;
    @NotBlank
    private String title;
	private String description;
}
