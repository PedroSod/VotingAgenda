package com.agendavoting.repository;


import com.agendavoting.model.VotingSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingSessionRepository extends MongoRepository<VotingSession, String> {

    Optional<VotingSession> findByAgendaId(String agendaId);
    boolean existsByAgendaId(String agendaId);
}
