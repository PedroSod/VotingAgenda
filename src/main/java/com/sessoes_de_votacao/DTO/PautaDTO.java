package com.sessoes_de_votacao.DTO;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class PautaDTO implements Serializable {

	private static final long serialVersionUID = 3184317326177799715L;
	private String descricao;
    @NotBlank
    private String titulo;
}
