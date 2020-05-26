package com.agendavoting.repository;


import com.agendavoting.model.SessionVotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionVotesRepository extends MongoRepository<SessionVotes, String>, CustomSessionVotesRepository {


    void deleteByVotingSessionId(String votingSessionId);

    void deleteByVotingSessionAgendaId(String AgendaId);

    Optional<SessionVotes> findByVotingSessionAgendaId(String agendaId);

    boolean existsByVotingSessionIdAndAllSessionVotesCpf(String id, String cpf);

}
