package com.agendavoting.DTO;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VotingSessionOutputDTO  implements Serializable {


    private static final long serialVersionUID = 2436815296747980242L;
    private String id;
    private AgendaOutputDTO agenda;
    private LocalDateTime start;
    private LocalDateTime end;
}
