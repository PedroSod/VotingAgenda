package com.agendavoting.service;

import com.agendavoting.enums.VotingOption;
import com.agendavoting.exception.DuplicateVoteException;
import com.agendavoting.model.Vote;
import com.agendavoting.repository.VoteRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void cast(String votingSessionId, String cpf, VotingOption votingOption) {
        try {
            voteRepository.insert(Vote.builder()
                    .votingSessionId(votingSessionId)
                    .cpf(cpf)
                    .votingOption(votingOption)
                    .build());
        } catch (DuplicateKeyException exception) {
            throw new DuplicateVoteException(cpf);
        }
    }

    public long countBySessionAndOption(String votingSessionId, VotingOption votingOption) {
        return voteRepository.countByVotingSessionIdAndVotingOption(votingSessionId, votingOption);
    }

    public void deleteByVotingSessionId(String votingSessionId) {
        voteRepository.deleteByVotingSessionId(votingSessionId);
    }
}
