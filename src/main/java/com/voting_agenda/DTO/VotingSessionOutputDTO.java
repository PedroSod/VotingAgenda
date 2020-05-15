package com.voting_agenda.DTO;

import com.voting_agenda.model.Agenda;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class VotingSessionOutputDTO  implements Serializable {


    private static final long serialVersionUID = 2436815296747980242L;
    private String id;
    private Agenda agenda;
    private LocalDateTime start;
    private LocalDateTime end;
}
