package com.sessoes_de_votacao.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "Pauta")
public class Pauta implements Serializable {


    private static final long serialVersionUID = 8827127855599510984L;
    @Id
    private Long id;
    private String descricao;
    private String titulo;


}
