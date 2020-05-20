package com.votingAgenda.business;

import com.votingAgenda.DTO.VoteDTO;
import com.votingAgenda.DTO.VotingResultDTO;
import com.votingAgenda.DTO.VotingSessionInputDTO;
import com.votingAgenda.enums.Status;
import com.votingAgenda.enums.VotingOption;
import com.votingAgenda.exception.DuplicateVoteException;
import com.votingAgenda.exception.VotingClosedException;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.model.SessionVotes;
import com.votingAgenda.model.Vote;
import com.votingAgenda.model.VotingSession;
import com.votingAgenda.restClient.CPFConsultationClient;
import com.votingAgenda.service.AgendaService;
import com.votingAgenda.service.SessionVotesService;
import com.votingAgenda.service.VotingSessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    private static CPFConsultationClient cpfConsultationClient;

    private static String TEST_ID = "testId";

    private static SessionVoteBusiness sessionVoteBusiness;

    @BeforeAll
    public static void setUp() {
        agendaService = Mockito.mock(AgendaService.class);
        sessionVotesService = Mockito.mock(SessionVotesService.class);
        votingSessionService = Mockito.mock(VotingSessionService.class);
        cpfConsultationClient = Mockito.mock(CPFConsultationClient.class);
        sessionVoteBusiness = new SessionVoteBusiness(votingSessionService,
                agendaService, sessionVotesService, new ModelMapper(), cpfConsultationClient);
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(agendaService, sessionVotesService, votingSessionService, cpfConsultationClient);
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
        when(cpfConsultationClient.getStatus(eq(voteDTO.getCpf()))).thenReturn(Status.ABLE_TO_VOTE);

        when(sessionVotesService.existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()))).thenReturn(false);
        when(votingSessionService.findEndTime(eq(TEST_ID))).
                thenReturn(ZonedDateTime.now(ZoneId.of(("UTC"))).plusMinutes(60L));
        doNothing().when(sessionVotesService).pushVote(eq(TEST_ID), any(Vote.class));
        sessionVoteBusiness.toVote(voteDTO);
        verify(sessionVotesService).existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()));
        verify(votingSessionService).findEndTime(eq(TEST_ID));
        verify(sessionVotesService).pushVote(eq(TEST_ID), any(Vote.class));
        verify(cpfConsultationClient).getStatus(eq(voteDTO.getCpf()));
    }

    @Test
    public void toVoteDuplicateVoteExceptionTest() {
        VoteDTO voteDTO = generateVoteDTO();
        when(cpfConsultationClient.getStatus(eq(voteDTO.getCpf()))).thenReturn(Status.ABLE_TO_VOTE);

        when(sessionVotesService.existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()))).thenReturn(true);

        assertThrows(DuplicateVoteException.class, () ->
                        sessionVoteBusiness.toVote(voteDTO),
                "the CPF " + voteDTO.getCpf() + ", has already voted on this agenda.");
        verify(sessionVotesService).existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()));
        verify(cpfConsultationClient).getStatus(eq(voteDTO.getCpf()));
    }

    @Test
    public void toVoteVotingClosedExceptionTest() {
        VoteDTO voteDTO = generateVoteDTO();
        when(cpfConsultationClient.getStatus(eq(voteDTO.getCpf()))).thenReturn(Status.ABLE_TO_VOTE);

        when(sessionVotesService.existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()))).thenReturn(false);

        when(votingSessionService.findEndTime(eq(TEST_ID))).
                thenReturn(ZonedDateTime.now(ZoneId.of(("UTC"))).minusMinutes(60L));

        assertThrows(VotingClosedException.class, () ->
                        sessionVoteBusiness.toVote(voteDTO),
                "The voting session " + TEST_ID + " has ended");
        verify(sessionVotesService).existsByIdAndAllSessionVotesCpf(
                eq(TEST_ID), eq(voteDTO.getCpf()));
        verify(votingSessionService).findEndTime(eq(TEST_ID));
        verify(cpfConsultationClient).getStatus(eq(voteDTO.getCpf()));

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
                .start(ZonedDateTime.now(ZoneId.of(("UTC"))))
                .end(ZonedDateTime.now(ZoneId.of(("UTC"))).plusMinutes(60L)).build();
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
                .start(ZonedDateTime.now(ZoneId.of(("UTC"))))
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