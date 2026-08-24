package com.agendavoting.repository;

import com.agendavoting.enums.VotingOption;
import com.agendavoting.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<Vote, String> {
    long countByVotingSessionIdAndVotingOption(String votingSessionId, VotingOption votingOption);
    void deleteByVotingSessionId(String votingSessionId);
}
