package com.votingAgenda.service;

import com.votingAgenda.exception.RecordNotFoundException;
import com.votingAgenda.model.VotingSession;
import com.votingAgenda.repository.VotingSessionRepository;
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

    public VotingSession save(VotingSession votingSession) {
        return votingSessionRepository.save(votingSession);
    }

    public VotingSession findById(String id) {
        return votingSessionRepository.findById(id).
                orElseThrow(() -> new RecordNotFoundException(id));
    }

    public LocalDateTime findEndTime(String id) {
        VotingSession votingSession = votingSessionRepository.findEndById(id).
                orElseThrow(() -> new RecordNotFoundException(id));
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
