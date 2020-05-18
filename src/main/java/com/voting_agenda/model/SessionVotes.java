package com.voting_agenda.model;


import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Collection;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionVotes implements Serializable {

    private static final long serialVersionUID = -5013268988160474124L;
    @Id
    private String id;
    private VotingSession votingSession;
    private Collection<Vote> allSessionVotes;
}
