package com.agendavoting.repository;

import com.agendavoting.model.Vote;

public interface CustomSessionVotesRepository {
    void pushVote(String agendaId, Vote vote);
}
