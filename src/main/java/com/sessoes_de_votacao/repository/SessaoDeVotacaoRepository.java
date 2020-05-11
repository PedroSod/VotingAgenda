package com.sessoes_de_votacao.repository;


import com.sessoes_de_votacao.model.SessaoDeVotacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoDeVotacaoRepository extends MongoRepository<SessaoDeVotacao, String> {
    
}
