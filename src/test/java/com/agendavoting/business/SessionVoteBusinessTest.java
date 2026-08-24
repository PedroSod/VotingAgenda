package com.agendavoting.business;

import com.agendavoting.dto.VoteDTO;
import com.agendavoting.dto.VotingResultDTO;
import com.agendavoting.dto.VotingSessionInputDTO;
import com.agendavoting.enums.Status;
import com.agendavoting.enums.VotingOption;
import com.agendavoting.exception.VotingClosedException;
import com.agendavoting.model.Agenda;
import com.agendavoting.model.VotingSession;
import com.agendavoting.restClient.CPFConsultationClient;
import com.agendavoting.service.AgendaService;
import com.agendavoting.service.VoteService;
import com.agendavoting.service.VotingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionVoteBusinessTest {
    @Mock private VotingSessionService votingSessionService;
    @Mock private AgendaService agendaService;
    @Mock private VoteService voteService;
    @Mock private CPFConsultationClient cpfConsultationClient;
    private SessionVoteBusiness business;

    @BeforeEach
    void setUp() {
        business = new SessionVoteBusiness(votingSessionService, agendaService, voteService, cpfConsultationClient);
    }

    @Test
    void startsDefaultOneMinuteSession() {
        Agenda agenda = Agenda.builder().id("agenda-1").build();
        when(agendaService.findById("agenda-1")).thenReturn(agenda);
        when(votingSessionService.save(any())).thenAnswer(invocation -> {
            VotingSession session = invocation.getArgument(0);
            session.setId("session-1");
            return session;
        });

        String id = business.startVotingSession(new VotingSessionInputDTO("agenda-1", null, null));

        assertEquals("session-1", id);
        verify(votingSessionService).save(argThat(session -> session.getAgenda().equals(agenda)
                && session.getEnd().equals(session.getStart().plusMinutes(1))));
    }

    @Test
    void castsVoteAgainstTheSessionFoundByAgenda() {
        VoteDTO vote = new VoteDTO("agenda-1", "91693816075", VotingOption.YES);
        when(cpfConsultationClient.getStatus(vote.cpf())).thenReturn(Status.ABLE_TO_VOTE);
        when(votingSessionService.findByAgendaId("agenda-1")).thenReturn(openSession());

        business.toVote(vote);

        verify(voteService).cast("session-1", vote.cpf(), VotingOption.YES);
    }

    @Test
    void rejectsClosedSession() {
        VoteDTO vote = new VoteDTO("agenda-1", "91693816075", VotingOption.YES);
        when(cpfConsultationClient.getStatus(vote.cpf())).thenReturn(Status.ABLE_TO_VOTE);
        VotingSession closed = openSession();
        closed.setEnd(LocalDateTime.now().minusSeconds(1));
        when(votingSessionService.findByAgendaId("agenda-1")).thenReturn(closed);

        assertThrows(VotingClosedException.class, () -> business.toVote(vote));
        verifyNoInteractions(voteService);
    }

    @Test
    void returnsDatabaseCounts() {
        when(votingSessionService.findByAgendaId("agenda-1")).thenReturn(openSession());
        when(voteService.countBySessionAndOption("session-1", VotingOption.YES)).thenReturn(3L);
        when(voteService.countBySessionAndOption("session-1", VotingOption.NO)).thenReturn(2L);

        assertEquals(new VotingResultDTO(3L, 2L, 5L), business.getVotingResult("agenda-1"));
    }

    private VotingSession openSession() {
        return VotingSession.builder().id("session-1").end(LocalDateTime.now().plusMinutes(1)).build();
    }
}
