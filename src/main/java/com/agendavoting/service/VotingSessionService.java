package com.agendavoting.service;

import com.agendavoting.exception.ExistingSessionException;
import com.agendavoting.exception.RecordNotFoundException;
import com.agendavoting.model.VotingSession;
import com.agendavoting.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class VotingSessionService {

    private final VotingSessionRepository votingSessionRepository;
    private final VoteService voteService;

    public VotingSessionService(VotingSessionRepository votingSessionRepository, VoteService voteService) {
        this.votingSessionRepository = votingSessionRepository;
        this.voteService = voteService;
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

    public VotingSession findByAgendaId(String agendaId) {
        return votingSessionRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> new RecordNotFoundException(agendaId));
    }

    public void delete(String id) {
        findById(id);
        voteService.deleteByVotingSessionId(id);
        votingSessionRepository.deleteById(id);
    }


    public void deleteByAgendaId(String id) {
        votingSessionRepository.findByAgendaId(id).ifPresent(session -> {
            voteService.deleteByVotingSessionId(session.getId());
            votingSessionRepository.deleteById(session.getId());
        });
    }

    public boolean existsByAgendaId(String id) {
        return votingSessionRepository.existsByAgendaId(id);
    }
}
