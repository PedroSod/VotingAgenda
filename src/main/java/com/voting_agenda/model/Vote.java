package com.voting_agenda.model;


import com.voting_agenda.enums.VotingOption;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class Vote implements Serializable {


    private static final long serialVersionUID = 8015087009277221679L;
    private String cpf;
    private VotingOption votingOption;
}