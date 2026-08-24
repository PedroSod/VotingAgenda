package com.agendavoting.dto;

public record VotingResultDTO(
        Long yesVotes,
        Long noVotes,
        Long totalVotes
) {
}
