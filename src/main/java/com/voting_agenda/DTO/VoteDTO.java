package com.voting_agenda.DTO;

import com.voting_agenda.enums.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class VoteDTO implements Serializable {

    private static final long serialVersionUID = -5427563818453409305L;
    private String votingSessionId;
    private String cpf;
    private VotingOption votingOption;
}