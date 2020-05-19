package com.votingAgenda.DTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class VotingResultDTO {

    private Long yesVotes;
    private Long noVotes;
    private Long totalVotes;
}
