package com.agendavoting.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VotingSessionInputDTO implements Serializable {


    private static final long serialVersionUID = 2436815296747980242L;
    @NotBlank
    private String agendaId;
    private LocalDateTime start;
    private Long timeDuration;

}
