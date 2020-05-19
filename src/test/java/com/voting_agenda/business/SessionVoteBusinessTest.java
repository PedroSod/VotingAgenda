package com.voting_agenda.business;

import com.voting_agenda.DTO.VoteDTO;
import com.voting_agenda.DTO.VotingResultDTO;
import com.voting_agenda.DTO.VotingSessionInputDTO;
import com.voting_agenda.enums.VotingOption;
import com.voting_agenda.exception.DuplicateVoteException;
import com.voting_agenda.exception.VotingClosedException;
import com.voting_agenda.model.Agenda;
import com.voting_agenda.model.SessionVotes;
import com.voting_agenda.model.Vote;
import com.voting_agenda.model.VotingSession;
import com.voting_agenda.service.AgendaService;
import com.voting_agenda.service.SessionVotesService;
import com.voting_agenda.service.VotingSessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SessionVoteBusinessTest {

    private static VotingSessionService votingSessionService;

    private static AgendaService agendaService;

    private static SessionVotesService sessionVotesService;

    private static String TEST_ID = "testId";

    private static SessionVoteBusiness sessionVoteBusiness;

    @BeforeAll
    public static void setUp() {
        agendaService = Mockito.mock(AgendaService.class);
        sessionVotesService = Mockito.mock(SessionVotesService.class);
        votingSessionService = Mockito.mock(VotingSessionService.class);
        sessionVoteBusiness = new SessionVoteBusiness(votingSessionService,
                agendaService, sessionVotesService, new ModelMapper());
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(agendaService, sessionVotesService, votingSessionService);
    }

    @Test
    public void startVotingSessionIncompleteObject() {
        VotingSessionInputDTO votingSessionInputDTO = new VotingSessionInputDTO();
        votingSessionInputDTO.setAgendaId(TEST_ID);
        Agenda agendaMock = generateAgenda();
        VotingSession votingSessionMock = generateVotingSession();

        when(agendaService.findById(eq(TEST_ID))).thenReturn(agendaMock);
        when(votingSessionService.save(any(VotingSession.class))).thenReturn(votingSessionMock);
        assertEquals(TEST_ID, sessionVoteBusiness.startVotingSession(votingSessionInputDTO));
        verify(agendaService).findById(eq(TEST_ID));
        verify(votingSessionService).save(any(VotingSession.class));

    }

    @Test
    public void startVotingSession() {
        VotingSessionInputDTO votingSessionInputDTO = generateVotingSessionInputDTO();
        Agenda agendaMock = generateAgenda();
        VotingSession votingSessionMock = generateVotingSession();

        when(agendaService.findById(eq(TEST_ID))).thenReturn(agendaMock);
        when(votingSessionService.save(any(VotingSession.class))).thenReturn(votingSessionMock);
        assertEquals(TEST_ID, sessionVoteBusiness.startVotingSession(votingSessionInputDTO));
        verify(agendaService).findById(eq(TEST_ID));
        verify(votingSessionService).save(any(VotingSession.class));
    }

    @Test
    public void toVoteSuccessTest() {
        VoteDTO voteDTO = generateVoteDTO();
        when(sessionVotesService.existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()))).thenReturn(false);
        when(votingSessionService.findEndTime(eq(TEST_ID))).
                thenReturn(LocalDateTime.now().plusMinutes(60L));
        doNothing().when(sessionVotesService).pushVote(eq(TEST_ID), any(Vote.class));
        sessionVoteBusiness.toVote(voteDTO);
        verify(sessionVotesService).existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()));
        verify(votingSessionService).findEndTime(eq(TEST_ID));
        verify(sessionVotesService).pushVote(eq(TEST_ID), any(Vote.class));
    }

    @Test
    public void toVoteDuplicateVoteExceptionTest() {
        VoteDTO voteDTO = generateVoteDTO();
        when(sessionVotesService.existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()))).thenReturn(true);

        assertThrows(DuplicateVoteException.class, () ->
                        sessionVoteBusiness.toVote(voteDTO),
                "the CPF " + voteDTO.getCpf() + ", has already voted on this agenda.");
        verify(sessionVotesService).existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()));
    }

    @Test
    public void toVoteVotingClosedExceptionTest() {
        VoteDTO voteDTO = generateVoteDTO();
        when(sessionVotesService.existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()))).thenReturn(false);

        when(votingSessionService.findEndTime(eq(TEST_ID))).
                thenReturn(LocalDateTime.now().minusMinutes(60L));

        assertThrows(VotingClosedException.class, () ->
                        sessionVoteBusiness.toVote(voteDTO),
                "The voting session " + TEST_ID + " has ended");
        verify(sessionVotesService).existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()));
        verify(votingSessionService).findEndTime(eq(TEST_ID));
    }

    @Test
    void getVotingResult() {
        when(sessionVotesService.findVotesByAgendaId(TEST_ID))
                .thenReturn(Collections.singletonList(generateVote()));

        VotingResultDTO votingResultDTO = generateVotingResultDTO();
        VotingResultDTO votingResultDTOReturned = sessionVoteBusiness.getVotingResult(TEST_ID);
        assertEquals(votingResultDTO, votingResultDTOReturned);
        verify(sessionVotesService).findVotesByAgendaId(eq(TEST_ID));
    }

    private static Agenda generateAgenda() {
        return new Agenda().builder()
                .id(TEST_ID)
                .title("testTitle")
                .description("test description")
                .build();
    }

    private static VotingSession generateVotingSession() {
        return new VotingSession().builder()
                .id(TEST_ID)
                .agenda(generateAgenda())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(60L)).build();
    }

    private static SessionVotes generateSessionVotes() {
        return new SessionVotes().builder()
                .id(TEST_ID)
                .votingSession(generateVotingSession())
                .allSessionVotes(Collections.emptyList()).build();

    }

    public static VotingSessionInputDTO generateVotingSessionInputDTO() {
        return new VotingSessionInputDTO().builder()
                .agendaId(TEST_ID)
                .start(LocalDateTime.now())
                .timeDuration(60L)
                .build();
    }

    public static VoteDTO generateVoteDTO() {
        return new VoteDTO().builder()
                .agendaId(TEST_ID)
                .cpf("91693816075")
                .votingOption(VotingOption.YES)
                .build();
    }

    private static Vote generateVote() {
        return new Vote().builder()
                .cpf("91693816075")
                .votingOption(VotingOption.YES)
                .build();
    }

    private static VotingResultDTO generateVotingResultDTO() {
        return new VotingResultDTO()
                .builder()
                .yesVotes(1L)
                .noVotes(0L)
                .totalVotes(1L)
                .build();
    }
}