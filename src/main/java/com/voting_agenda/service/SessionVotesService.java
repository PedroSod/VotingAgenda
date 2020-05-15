package com.voting_agenda.service;

import com.voting_agenda.exception.RecordNotFoundException;
import com.voting_agenda.model.SessionVotes;
import com.voting_agenda.repository.SessionVotesRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class SessionVotesService {

    private SessionVotesRepository sessionVotesRepository;

    public SessionVotesService(SessionVotesRepository sessionVotesRepository) {
        this.sessionVotesRepository = sessionVotesRepository;
    }

    public SessionVotes save(SessionVotes sessionVotes) {
        return sessionVotesRepository.save(sessionVotes);
    }

    public Collection<SessionVotes> findAll() {
        return sessionVotesRepository.findAll();
    }

//    public Collection<SessionVotes> findAllByAgendaId(String agendaId) {
//        return sessionVotesRepository.findAllByAgendaId(agendaId);
//    }

    public SessionVotes findAllBySessionId(String votingSessionId) {
        return sessionVotesRepository.findByVotingSessionId(votingSessionId)
                .orElseThrow(() -> new RecordNotFoundException(votingSessionId));

    }
    public void update(SessionVotes sessionVotes) {
        sessionVotesRepository.save(sessionVotes);
    }
    public boolean existsByIdAndAllSessionVotesCpf(String id, String cpf){
        return sessionVotesRepository.existsByVotingSessionIdAndAllSessionVotesCpf(id, cpf);
    }
}
