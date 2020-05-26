package com.agendavoting.repository;


import com.agendavoting.model.VotingSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingSessionRepository extends MongoRepository<VotingSession, String> {

    void deleteByAgendaId(String idAgenda);
    @Query(fields="{end : 1}")
    Optional<VotingSession> findEndById(String id);
    boolean existsByAgendaId(String agendaId);
}
