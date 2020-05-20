package com.votingAgenda.service;

import com.votingAgenda.enums.VotingOption;
import com.votingAgenda.exception.RecordNotFoundException;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.model.SessionVotes;
import com.votingAgenda.model.Vote;
import com.votingAgenda.model.VotingSession;
import com.votingAgenda.repository.SessionVotesRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SessionVotesServiceTest {

    @Mock
    private SessionVotesRepository sessionVotesRepository;

    @InjectMocks
    private SessionVotesService sessionVotesService;

    private static final String TEST_ID = "testId";
    private static SessionVotes sessionVotesMock;
    private static Vote vote;

    @BeforeAll
    public static void setUp() {
        vote = generateVote();
        sessionVotesMock = generateSessionVotes();
    }

    @Test
    void saveSuccessTest() {
        when(sessionVotesRepository.save(eq(sessionVotesMock))).thenReturn(sessionVotesMock);
        SessionVotes sessionVotesReturned = sessionVotesService.save(sessionVotesMock);
        assertEquals(sessionVotesMock, sessionVotesReturned);
        verify(sessionVotesRepository).save(eq(sessionVotesMock));
    }

    @Test
    void pushVoteSuccessTest() {
        doNothing().when(sessionVotesRepository).pushVote(eq(TEST_ID), eq(vote));
        sessionVotesService.pushVote(TEST_ID, vote);
        verify(sessionVotesRepository).pushVote(eq(TEST_ID), eq(vote));
    }

    @Test
    void existsByIdAndAllSessionVotesCpfSuccessTest() {
        when(sessionVotesRepository.existsByVotingSessionIdAndAllSessionVotesCpf
                (eq(TEST_ID), eq(vote.getCpf()))).thenReturn(true);
        assertTrue(sessionVotesService.existsByIdAndAllSessionVotesCpf(TEST_ID, vote.getCpf()));
    }

    @Test
    void findVotesByAgendaIdSuccessTest() {
        Optional<SessionVotes> optionalMock = Optional.ofNullable(sessionVotesMock);
        when(sessionVotesRepository.findByVotingSessionAgendaId(eq(TEST_ID))).thenReturn(optionalMock);
        Collection<Vote> allVotes = sessionVotesService.findVotesByAgendaId(TEST_ID);
        assertEquals(sessionVotesMock.getAllSessionVotes(), allVotes);
        verify(sessionVotesRepository).findByVotingSessionAgendaId(eq(TEST_ID));
    }

    @Test
    void findVotesByAgendaIdRecordNotFoundExceptionTest() {
        assertThrows(RecordNotFoundException.class, () ->
                sessionVotesService.findVotesByAgendaId(TEST_ID), "No record found for id : " + TEST_ID);
    }

    @Test
    void deleteByVotingSessionIdSuccessTest() {
        doNothing().when(sessionVotesRepository).deleteByVotingSessionId(eq(TEST_ID));
        sessionVotesService.deleteByVotingSessionId(TEST_ID);
        verify(sessionVotesRepository).deleteByVotingSessionId(eq(TEST_ID));
    }

    @Test
    void deleteByVotingSessionAgendaIdSuccessTest() {
        doNothing().when(sessionVotesRepository).deleteByVotingSessionAgendaId(eq(TEST_ID));
        sessionVotesService.deleteByVotingSessionAgendaId(TEST_ID);
        verify(sessionVotesRepository).deleteByVotingSessionAgendaId(eq(TEST_ID));
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

    private static Vote generateVote() {
        return new Vote().builder()
                .cpf("91693816075")
                .votingOption(VotingOption.YES)
                .build();
    }

    private static SessionVotes generateSessionVotes() {
        return new SessionVotes().builder()
                .id(TEST_ID)
                .votingSession(generateVotingSession())
                .allSessionVotes(Collections.singletonList(vote)).build();

    }
}