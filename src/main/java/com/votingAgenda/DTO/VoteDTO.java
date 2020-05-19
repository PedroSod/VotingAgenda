package com.votingAgenda.DTO;

import com.votingAgenda.enums.VotingOption;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoteDTO implements Serializable {

    private static final long serialVersionUID = -5427563818453409305L;
    private String agendaId;
    @CPF
    private String cpf;
    @NotNull
    private VotingOption votingOption;
}