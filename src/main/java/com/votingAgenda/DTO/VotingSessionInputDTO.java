package com.votingAgenda.DTO;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VotingSessionInputDTO implements Serializable {


    private static final long serialVersionUID = 2436815296747980242L;
    private String agendaId;
    private ZonedDateTime start;
    private Long timeDuration;

}
