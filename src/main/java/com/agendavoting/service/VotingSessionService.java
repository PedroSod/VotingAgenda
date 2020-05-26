package com.agendavoting.service;

import com.agendavoting.exception.ExistingSessionException;
import com.agendavoting.exception.RecordNotFoundException;
import com.agendavoting.model.VotingSession;
import com.agendavoting.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotingSessionService {

    private final VotingSessionRepository votingSessionRepository;
    private final SessionVotesService sessionVotesService;

    public VotingSessionService(VotingSessionRepository votingSessionRepository, SessionVotesService sessionVotesRepository) {
        this.votingSessionRepository = votingSessionRepository;
        this.sessionVotesService = sessionVotesRepository;
    }

    public VotingSession save(VotingSession votingSession) {
        if (votingSessionRepository.existsByAgendaId(votingSession.getAgenda().getId())) {
            throw new ExistingSessionException();
        }
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
