package com.sessoes_de_votacao.DTO;

import com.sessoes_de_votacao.model.SessaoDeVotacao;
import com.sessoes_de_votacao.model.Voto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
public class VotosSessaoDTO implements Serializable {


    private SessaoDeVotacao sessaoDeVotacao;
    private Voto voto;
    private Collection<Voto> todosVotos;

}
