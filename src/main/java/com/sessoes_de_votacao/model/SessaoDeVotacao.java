package com.sessoes_de_votacao.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "SessaoDeVotacao")
public class SessaoDeVotacao implements Serializable {

	private static final long serialVersionUID = 1777612561801338066L;
	@Id
    private Long id;
    private Pauta pauta;
    private LocalDateTime inicio;
    private Long duracao;
}
