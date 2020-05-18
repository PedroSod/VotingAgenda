package com.voting_agenda.DTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class VotingResultDTO {

    private Long yesVotes;
    private Long noVotes;
    private Long totalVotes;
}
