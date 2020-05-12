package com.voting_agenda.DTO;

import com.voting_agenda.model.VotingSession;
import com.voting_agenda.model.Vote;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
public class SessionVotesDTO implements Serializable {


    private VotingSession votingSession;
    private Vote vote;
    private Collection<Vote> allSessionVotes;

}
