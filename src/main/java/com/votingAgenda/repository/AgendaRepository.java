package com.votingAgenda.repository;


import com.votingAgenda.model.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {

}
