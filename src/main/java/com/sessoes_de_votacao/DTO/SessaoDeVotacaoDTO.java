package com.sessoes_de_votacao.DTO;

import com.sessoes_de_votacao.model.Pauta;
import com.sessoes_de_votacao.model.Voto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
public class SessaoDeVotacaoDTO implements Serializable {


    private static final long serialVersionUID = 2436815296747980242L;
    private Pauta pauta;
    private LocalDateTime inicio;
    private Long duracao;

}
