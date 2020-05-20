package com.votingAgenda.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.ZonedDateTime;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VotingSession implements Serializable {

    private static final long serialVersionUID = 1777612561801338066L;
    @Id
    private String id;
    private Agenda agenda;
    private ZonedDateTime start;
    private ZonedDateTime end;
}
