package com.sessoes_de_votacao.enums;

public enum OpcaoDeVoto {

    SIM("sim"), NAO("não");

    private String descricao;

    OpcaoDeVoto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
