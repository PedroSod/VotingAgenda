package com.sessoes_de_votacao.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class PautaDTO implements Serializable {

	private static final long serialVersionUID = 3184317326177799715L;
	private String id;
	private String descricao;
    @NotBlank
    private String titulo;
}
