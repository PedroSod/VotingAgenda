package com.voting_agenda.repository;


import com.voting_agenda.model.SessionVotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionVotesRepository extends MongoRepository<SessionVotes, String> {

}
