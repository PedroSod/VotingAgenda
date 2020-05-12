package com.voting_agenda.DTO;

import com.voting_agenda.model.Agenda;
import com.voting_agenda.model.Vote;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "VotosPauta")
public class AgendaVotesDTO implements Serializable {

    private static final long serialVersionUID = -1522401421611582303L;
    private Agenda agenda;
    private Collection<Vote> allAgendaVotes;

}
