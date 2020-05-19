package com.votingAgenda.service;

import com.votingAgenda.exception.BadRequestException;
import com.votingAgenda.exception.RecordNotFoundException;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.repository.AgendaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;
    @Mock
    private VotingSessionService votingSessionService;
    @InjectMocks
    private AgendaService agendaService;

    private static Agenda agendaMock;

    private static String TEST_ID = "testId";

    @BeforeAll
    public static void setUp() {
        agendaMock = generateAgenda();
    }

    @Test
    public void saveSuccessTest() {
        when(agendaRepository.save(eq(agendaMock))).thenReturn(agendaMock);
        Agenda agendaReturned = agendaService.save(agendaMock);
        assertEquals(agendaMock, agendaReturned);
        verify(agendaRepository).save(eq(agendaMock));
    }

    @Test
    public void findByIdSuccessTest() {
        Optional<Agenda> optionalMock = Optional.ofNullable(agendaMock);
        when(agendaRepository.findById(eq(TEST_ID))).thenReturn(optionalMock);
        Agenda agendaReturned = agendaService.findById(TEST_ID);
        assertEquals(agendaMock, agendaReturned);
        verify(agendaRepository).findById(eq(TEST_ID));
    }

    @Test
    public void findByIdNotFoundTest() {
        assertThrows(RecordNotFoundException.class, () ->
                        agendaService.findById(TEST_ID),
                "No record found for id : " + TEST_ID);
    }

    @Test
    public void findAllTest() {
        List<Agenda> mockCollection = Collections.singletonList(agendaMock);
        when(agendaRepository.findAll()).thenReturn(mockCollection);
        Collection<Agenda> allAgendas = agendaService.findAll();
        assertEquals(mockCollection, allAgendas);
        verify(agendaRepository).findAll();
    }

    @Test
    public void deleteSuccessTest() {
        doNothing().when(agendaRepository).deleteById(eq(TEST_ID));
        doNothing().when(votingSessionService).deleteByAgendaId(eq(TEST_ID));
        agendaService.delete(TEST_ID);
        verify(agendaRepository).deleteById(eq(TEST_ID));
        verify(votingSessionService).deleteByAgendaId(eq(TEST_ID));
    }

    @Test
    public void updateSuccessTest() {
        Optional<Agenda> optionalMock = Optional.ofNullable(agendaMock);
        Agenda agendaToUpdate = new Agenda(TEST_ID, "update", "updated");
        when(agendaRepository.findById(eq(TEST_ID))).thenReturn(optionalMock);
        when(agendaRepository.save(eq(agendaToUpdate))).thenReturn(agendaToUpdate);
        when(votingSessionService.existsByAgendaId(eq(TEST_ID))).thenReturn(false);
        agendaService.update(TEST_ID, agendaToUpdate);
        verify(agendaRepository).findById(eq(TEST_ID));
        verify(agendaRepository).save(eq(agendaToUpdate));
    }

    @Test
    public void updateNotFoundExceptionTest() {
        assertThrows(RecordNotFoundException.class, () ->
                        agendaService.update(TEST_ID, agendaMock),
                "No record found for id : " + TEST_ID);
    }

    @Test
    public void updateBadRequestExceptionTest() {
        Optional<Agenda> optionalMock = Optional.ofNullable(agendaMock);
        when(agendaRepository.findById(eq(TEST_ID))).thenReturn(optionalMock);
        when(votingSessionService.existsByAgendaId(eq(TEST_ID))).thenReturn(true);
        assertThrows(BadRequestException.class, () ->
                        agendaService.update(TEST_ID, agendaMock),
                "Unable to update agenda that with voting session started.");
    }

    private static Agenda generateAgenda() {
        return new Agenda().builder()
                .id(TEST_ID)
                .title("testTitle")
                .description("test description")
                .build();
    }
}