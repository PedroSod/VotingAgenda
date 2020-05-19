package com.votingAgenda.repository;

import com.votingAgenda.model.Vote;

public interface CustomSessionVotesRepository {
    void pushVote(String agendaId, Vote vote);
}
