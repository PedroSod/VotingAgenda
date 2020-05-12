package com.voting_agenda.repository;


import com.voting_agenda.model.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {

}
