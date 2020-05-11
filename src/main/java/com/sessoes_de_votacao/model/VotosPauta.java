package com.sessoes_de_votacao.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "VotosPauta")
public class VotosPauta implements Serializable {

    private static final long serialVersionUID = -1522401421611582303L;
    private Pauta pauta;
    private Collection<Voto> todosVotos;

}
