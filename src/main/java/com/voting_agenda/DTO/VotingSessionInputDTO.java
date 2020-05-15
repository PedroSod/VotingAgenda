package com.voting_agenda.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class VotingSessionInputDTO implements Serializable {


    private static final long serialVersionUID = 2436815296747980242L;
    private String agendaId;
    private LocalDateTime start;
    private Long timeDuration;

}
