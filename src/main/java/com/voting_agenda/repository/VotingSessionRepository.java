package com.voting_agenda.repository;


import com.voting_agenda.model.VotingSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends MongoRepository<VotingSession, String> {
    
}
