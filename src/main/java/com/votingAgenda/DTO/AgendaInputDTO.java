package com.votingAgenda.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AgendaInputDTO implements Serializable {

	private static final long serialVersionUID = 3184317326177799715L;
    @NotBlank
    private String title;
	private String description;
}
