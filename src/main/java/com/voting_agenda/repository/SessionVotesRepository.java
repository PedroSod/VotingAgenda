package com.voting_agenda.repository;


import com.voting_agenda.model.SessionVotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SessionVotesRepository extends MongoRepository<SessionVotes, String> {

    Optional<SessionVotes> findByVotingSessionId(String idVotingSession);

    void deleteByVotingSessionId(String idVotingSession);

    Collection<SessionVotes> findAllByVotingSessionAgendaId(String agendaId);

    boolean existsByVotingSessionIdAndAllSessionVotesCpf(String id, String cpf);
}
