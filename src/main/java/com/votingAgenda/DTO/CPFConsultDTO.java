package com.votingAgenda.DTO;

import com.votingAgenda.enums.Status;
import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CPFConsultDTO {
    private Status status;
}
