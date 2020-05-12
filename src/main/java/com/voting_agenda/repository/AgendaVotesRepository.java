package com.voting_agenda.repository;


import com.voting_agenda.model.AgendaVotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaVotesRepository extends MongoRepository<AgendaVotes, String> {

}
