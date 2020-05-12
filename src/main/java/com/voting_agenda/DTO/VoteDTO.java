package com.voting_agenda.DTO;

import com.voting_agenda.enums.*;

import java.io.Serializable;


public class VoteDTO implements Serializable {

    private static final long serialVersionUID = -5427563818453409305L;
    private String cpf;
    private VotingOption votingOption;
}