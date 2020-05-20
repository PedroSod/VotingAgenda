package com.votingAgenda.service;

import com.votingAgenda.exception.ExistingSessionException;
import com.votingAgenda.exception.RecordNotFoundException;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.model.VotingSession;
import com.votingAgenda.repository.VotingSessionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class VotingSessionServiceTest {
    @Mock
    private VotingSessionRepository votingSessionRepository;
    @Mock
    private SessionVotesService sessionVotesService;
    @InjectMocks
    private VotingSessionService votingSessionService;

    private static final String TEST_ID = "testId";

    private static VotingSession votingSession;


    @BeforeAll
    public static void setUp() {
        Agenda agendaMock = generateAgenda();
        votingSession = generateVotingSession();
    }

    @Test
    void saveSuccessTest() {
        when(votingSessionRepository.save(eq(votingSession))).thenReturn(votingSession);
        VotingSession votingSessionReturned = votingSessionService.save(votingSession);
        assertEquals(votingSession, votingSessionReturned);
        verify(votingSessionRepository).save(eq(votingSession));
    }

    @Test
    public void createExistingSessionExceptionTest() {
        when(votingSessionRepository.existsByAgendaId(eq(TEST_ID))).thenReturn(true);
        assertThrows(ExistingSessionException.class, () ->
                        votingSessionService.save(votingSession),
                "There is already a session for this agenda.");
    }

    @Test
    void findByIdSuccessTest() {
        Optional<VotingSession> optionalMock = Optional.ofNullable(votingSession);
        when(votingSessionRepository.findById(eq(TEST_ID))).thenReturn(optionalMock);
        VotingSession votingSessionReturned = votingSessionService.findById(TEST_ID);
        assertEquals(votingSession, votingSessionReturned);
        verify(votingSessionRepository).findById(eq(TEST_ID));
    }

    @Test
    public void findByIdNotFoundTest() {
        assertThrows(RecordNotFoundException.class, () ->
                        votingSessionService.findById(TEST_ID),
                "No record found for id : " + TEST_ID);
    }

    @Test
    void findEndTimeSuccessTest() {
        Optional<VotingSession> optionalMock = Optional.ofNullable(votingSession);
        when(votingSessionRepository.findEndById(eq(TEST_ID))).thenReturn(optionalMock);
        LocalDateTime endTime = votingSessionService.findEndTime(TEST_ID);
        assertEquals(votingSession.getEnd(), endTime);
        verify(votingSessionRepository).findEndById(eq(TEST_ID));
    }

    @Test
    public void findEndTimeNotFoundTest() {
        assertThrows(RecordNotFoundException.class, () ->
                        votingSessionService.findEndTime(TEST_ID),
                "No record found for id : " + TEST_ID);
    }

    @Test
    void deleteSuccessTest() {
        doNothing().when(votingSessionRepository).deleteById(eq(TEST_ID));
        doNothing().when(sessionVotesService).deleteByVotingSessionId(eq(TEST_ID));
        votingSessionService.delete(TEST_ID);
        verify(votingSessionRepository).deleteById(eq(TEST_ID));
        verify(sessionVotesService).deleteByVotingSessionId(eq(TEST_ID));
    }

    @Test
    void deleteByAgendaId() {
        doNothing().when(votingSessionRepository).deleteByAgendaId(eq(TEST_ID));
        doNothing().when(sessionVotesService).deleteByVotingSessionAgendaId(eq(TEST_ID));
        votingSessionService.deleteByAgendaId(TEST_ID);
        verify(votingSessionRepository).deleteByAgendaId(eq(TEST_ID));
        verify(sessionVotesService).deleteByVotingSessionAgendaId(eq(TEST_ID));
    }

    @Test
    void existsByAgendaId() {
        when(votingSessionRepository.existsByAgendaId(eq(TEST_ID))).thenReturn(true);
        assertTrue(votingSessionService.existsByAgendaId(TEST_ID));
        verify(votingSessionRepository).existsByAgendaId(eq(TEST_ID));
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
}