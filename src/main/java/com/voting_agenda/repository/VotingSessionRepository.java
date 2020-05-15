package com.voting_agenda.repository;


import com.voting_agenda.model.Agenda;
import com.voting_agenda.model.VotingSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingSessionRepository extends MongoRepository<VotingSession, String> {

    void deleteByAgendaId(String idAgenda);

    Optional<VotingSession> findByAgenda(Agenda agenda);
}
