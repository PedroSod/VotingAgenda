package com.sessoes_de_votacao.model;


import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;

@Document(collection = "VotosSessao")
public class VotosSessao implements Serializable {

   private static final long serialVersionUID = -5013268988160474124L;
   private SessaoDeVotacao sessaoDeVotacao;
   private Collection<Voto> totalDeVotos;
}
