package com.sessoes_de_votacao.repository;


import com.sessoes_de_votacao.model.VotosPauta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotosPautaRepository extends MongoRepository<VotosPauta, String> {

}
