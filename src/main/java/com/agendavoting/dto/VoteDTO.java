package com.agendavoting.dto;

import com.agendavoting.enums.VotingOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

public record VoteDTO(
        @NotBlank String agendaId,
        @NotBlank @CPF String cpf,
        @NotNull VotingOption votingOption
) implements Serializable {
}
