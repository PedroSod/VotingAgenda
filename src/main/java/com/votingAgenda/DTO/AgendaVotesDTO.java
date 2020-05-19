package com.votingAgenda.DTO;

import com.votingAgenda.model.Agenda;
import com.votingAgenda.model.Vote;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
public class AgendaVotesDTO implements Serializable {

    private static final long serialVersionUID = -1522401421611582303L;
    private Agenda agenda;
    private Collection<Vote> allAgendaVotes;

}
