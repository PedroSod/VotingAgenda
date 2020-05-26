package com.agendavoting.DTO;

import com.agendavoting.enums.Status;
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
