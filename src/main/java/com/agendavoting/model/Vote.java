package com.agendavoting.model;


import com.agendavoting.enums.VotingOption;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document("votes")
@CompoundIndex(name = "session_cpf_unique", def = "{'votingSessionId': 1, 'cpf': 1}", unique = true)
public class Vote implements Serializable {


    @Serial
    private static final long serialVersionUID = 8015087009277221679L;
    @Id
    private String id;
    private String votingSessionId;
    private String cpf;
    private VotingOption votingOption;
}
