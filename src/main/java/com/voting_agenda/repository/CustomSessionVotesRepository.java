package com.voting_agenda.repository;

import com.voting_agenda.model.Vote;

public interface CustomSessionVotesRepository {
    void pushVote(String agendaId, Vote vote);
}
