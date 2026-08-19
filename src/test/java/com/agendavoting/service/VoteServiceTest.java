package com.agendavoting.service;

import com.agendavoting.enums.VotingOption;
import com.agendavoting.exception.DuplicateVoteException;
import com.agendavoting.model.Vote;
import com.agendavoting.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @Mock private VoteRepository voteRepository;
    @InjectMocks private VoteService voteService;

    @Test
    void castsVoteWithSessionAndCpf() {
        voteService.cast("session-1", "91693816075", VotingOption.YES);
        verify(voteRepository).insert(org.mockito.ArgumentMatchers.<Vote>argThat(vote -> vote.getVotingSessionId().equals("session-1")
                && vote.getCpf().equals("91693816075") && vote.getVotingOption() == VotingOption.YES));
    }

    @Test
    void mapsUniqueIndexViolationToDuplicateVote() {
        when(voteRepository.insert(any(Vote.class))).thenThrow(new DuplicateKeyException("duplicate"));
        assertThrows(DuplicateVoteException.class,
                () -> voteService.cast("session-1", "91693816075", VotingOption.YES));
    }

    @Test
    void countsVotesBySessionAndOption() {
        when(voteRepository.countByVotingSessionIdAndVotingOption("session-1", VotingOption.NO)).thenReturn(2L);
        assertEquals(2L, voteService.countBySessionAndOption("session-1", VotingOption.NO));
    }
}
