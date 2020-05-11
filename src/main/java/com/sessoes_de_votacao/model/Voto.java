package com.sessoes_de_votacao.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class Voto implements Serializable {


    private static final long serialVersionUID = 8015087009277221679L;
    private String cpf;
    private String opcaoDeVoto;
}