package com.voting_agenda.service;

import com.voting_agenda.exception.RecordNotFoundException;
import com.voting_agenda.model.VotingSession;
import com.voting_agenda.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotingSessionService {

    private VotingSessionRepository votingSessionRepository;
    private SessionVotesService sessionVotesService;

    public VotingSessionService(VotingSessionRepository votingSessionRepository, SessionVotesService sessionVotesRepository) {
        this.votingSessionRepository = votingSessionRepository;
        this.sessionVotesService = sessionVotesRepository;
    }

    public VotingSession save(VotingSession agenda) {
        return votingSessionRepository.save(agenda);
    }

    public VotingSession findById(String id) {
        return votingSessionRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public LocalDateTime findEndTime(String id) {
        VotingSession votingSession = votingSessionRepository.findEndById(id).orElseThrow(() -> new RecordNotFoundException(id));
        return votingSession.getEnd();
    }

    public void delete(String id) {
        votingSessionRepository.deleteById(id);
        sessionVotesService.deleteByVotingSessionId(id);
    }


    public void deleteByAgendaId(String id) {
        votingSessionRepository.deleteByAgendaId(id);
        sessionVotesService.deleteByVotingSessionAgendaId(id);
    }

    public boolean existsByAgendaId(String id) {
        return votingSessionRepository.existsByAgendaId(id);
    }
}
