package com.sessoes_de_votacao.DTO;

import com.sessoes_de_votacao.enums.*;

import java.io.Serializable;


public class VotoDTO implements Serializable {

    private static final long serialVersionUID = -5427563818453409305L;
    private String cpf;
    private OpcaoDeVoto opcaoDeVoto;
}