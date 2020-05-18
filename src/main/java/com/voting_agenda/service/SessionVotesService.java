package com.voting_agenda.service;

import com.voting_agenda.exception.RecordNotFoundException;
import com.voting_agenda.model.SessionVotes;
import com.voting_agenda.model.Vote;
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

    public void pushVote(String id, Vote vote) {
        sessionVotesRepository.pushVote(id, vote);
    }

    public boolean existsByIdAndAllSessionVotesCpf(String id, String cpf) {
        return sessionVotesRepository.existsByVotingSessionIdAndAllSessionVotesCpf(id, cpf);
    }

    public Collection<Vote> countByVotingSessionAgendaId(String agendaId) {
        SessionVotes sessionVotes = sessionVotesRepository.findByVotingSessionAgendaId(agendaId).
                orElseThrow(() -> new RecordNotFoundException(agendaId));
        return sessionVotes.getAllSessionVotes();
    }

    public void deleteByVotingSessionId(String votionSessionId) {
        sessionVotesRepository.deleteByVotingSessionId(votionSessionId);
    }

    public void deleteByVotingSessionAgendaId(String agendaId) {
        sessionVotesRepository.deleteByVotingSessionAgendaId(agendaId);

    }
}