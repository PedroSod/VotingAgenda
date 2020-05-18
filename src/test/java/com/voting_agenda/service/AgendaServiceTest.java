package com.voting_agenda.service;

import com.voting_agenda.exception.BadRequestException;
import com.voting_agenda.exception.RecordNotFoundException;
import com.voting_agenda.model.Agenda;
import com.voting_agenda.repository.AgendaRepository;
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
    AgendaRepository agendaRepository;
    @Mock
    VotingSessionService votingSessionService;
    @InjectMocks
    AgendaService agendaService;

    private static Agenda agendaMock;


    @BeforeAll
    public static void setUp() {
        agendaMock = new Agenda("idTest", "testTitle", "test description");
    }

    @Test
    public void save() {
        when(agendaRepository.save(eq(agendaMock))).thenReturn(agendaMock);
        Agenda agendaReturned = agendaService.save(agendaMock);
        assertEquals(agendaMock, agendaReturned);
        verify(agendaRepository).save(eq(agendaMock));
    }

    @Test
    public void findById() {
        Optional<Agenda> optionalMock = Optional.ofNullable(agendaMock);
        when(agendaRepository.findById(eq(agendaMock.getId()))).thenReturn(optionalMock);
        Agenda agendaReturned = agendaService.findById(agendaMock.getId());
        assertEquals(agendaMock, agendaReturned);
        verify(agendaRepository).findById(eq(agendaMock.getId()));
    }

    @Test
    public void findByIdNotFound() {
        assertThrows(RecordNotFoundException.class, () ->
                agendaService.findById(agendaMock.getId()), "No record found for id : " + agendaMock.getId());
    }

    @Test
    public void findAll() {
        List<Agenda> mockCollection = Collections.singletonList(agendaMock);
        when(agendaRepository.findAll()).thenReturn(mockCollection);
        Collection<Agenda> allAgendas = agendaService.findAll();
        assertEquals(mockCollection, allAgendas);
        verify(agendaRepository).findAll();
    }

    @Test
    public void delete() {
        Optional<Agenda> optionalMock = Optional.ofNullable(agendaMock);
        when(agendaRepository.findById(eq(agendaMock.getId()))).thenReturn(optionalMock);
        doNothing().when(agendaRepository).deleteById(eq(agendaMock.getId()));
        doNothing().when(votingSessionService).deleteByAgendaId(eq(agendaMock.getId()));
        agendaService.delete(agendaMock.getId());
        verify(agendaRepository).deleteById(eq(agendaMock.getId()));
        verify(agendaRepository).deleteById(eq(agendaMock.getId()));
        verify(votingSessionService).deleteByAgendaId(eq(agendaMock.getId()));
    }

    @Test
    public void update() {
        Optional<Agenda> optionalMock = Optional.ofNullable(agendaMock);
        Agenda agendaToUpdate = new Agenda(agendaMock.getId(), "update", "updated");
        when(agendaRepository.findById(eq(agendaMock.getId()))).thenReturn(optionalMock);
        when(agendaRepository.save(eq(agendaToUpdate))).thenReturn(agendaToUpdate);
        when(votingSessionService.existsByAgendaId(eq(agendaMock.getId()))).thenReturn(false);
        agendaService.update(agendaMock.getId(), agendaToUpdate);
        verify(agendaRepository).findById(eq(agendaMock.getId()));
        verify(agendaRepository).save(eq(agendaToUpdate));
    }
    @Test
    public void updateNotFound() {
        assertThrows(RecordNotFoundException.class, () ->
                agendaService.update(agendaMock.getId(), agendaMock), "No record found for id : " + agendaMock.getId());
    }
    @Test
    public void updateBadRequestException() {
        Optional<Agenda> optionalMock = Optional.ofNullable(agendaMock);
        when(agendaRepository.findById(eq(agendaMock.getId()))).thenReturn(optionalMock);
        when(votingSessionService.existsByAgendaId(eq(agendaMock.getId()))).thenReturn(true);
        assertThrows(BadRequestException.class, () ->
                agendaService.update(agendaMock.getId(), agendaMock), "Unable to update agenda that with voting session started.");
    }
}