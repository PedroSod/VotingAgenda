package com.agendavoting.repository;


import com.agendavoting.model.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {

}
