package com.voting_agenda.DTO;

import com.voting_agenda.enums.VotingOption;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class VoteDTO implements Serializable {

    private static final long serialVersionUID = -5427563818453409305L;
    private String agendaId;
    @CPF
    private String cpf;
    @NotBlank
    private VotingOption votingOption;
}