package com.voting_agenda.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class VotingSession implements Serializable {

    private static final long serialVersionUID = 1777612561801338066L;
    @Id
    private String id;
    private Agenda agenda;
    private LocalDateTime start;
    private LocalDateTime end;
}
