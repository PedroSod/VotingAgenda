package com.votingAgenda.model;


import com.votingAgenda.enums.VotingOption;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Vote implements Serializable {


    private static final long serialVersionUID = 8015087009277221679L;
    @CPF
    private String cpf;
    private VotingOption votingOption;
}