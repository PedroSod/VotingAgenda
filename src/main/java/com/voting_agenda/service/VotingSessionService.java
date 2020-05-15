package com.voting_agenda.service;

import com.voting_agenda.exception.RecordNotFoundException;
import com.voting_agenda.model.VotingSession;
import com.voting_agenda.repository.SessionVotesRepository;
import com.voting_agenda.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class VotingSessionService {

    private VotingSessionRepository votingSessionRepository;
    private SessionVotesRepository sessionVotesRepository;

    public VotingSessionService(VotingSessionRepository votingSessionRepository, SessionVotesRepository sessionVotesRepository) {
        this.votingSessionRepository = votingSessionRepository;
        this.sessionVotesRepository = sessionVotesRepository;
    }

    public VotingSession save(VotingSession agenda) {
        return votingSessionRepository.save(agenda);
    }

    public VotingSession findById(String id) {
        return votingSessionRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public Collection<VotingSession> findAll() {
        return votingSessionRepository.findAll();
    }

//    public void delete(String id) {
//        votingSessionRepository.deleteById(id);
//        sessionVotesRepository.deleteByVotingSessionId(id);
//    }


}
