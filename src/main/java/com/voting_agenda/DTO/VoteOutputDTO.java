package com.voting_agenda.DTO;


import com.voting_agenda.enums.VotingOption;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class VoteOutputDTO implements Serializable {


    private static final long serialVersionUID = 8015087009277221679L;
    @CPF
    private String cpf;
    private VotingOption votingOption;
}