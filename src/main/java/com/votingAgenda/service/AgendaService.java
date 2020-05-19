package com.votingAgenda.service;

import com.votingAgenda.exception.BadRequestException;
import com.votingAgenda.exception.RecordNotFoundException;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AgendaService {

    private AgendaRepository agendaRepository;
    private VotingSessionService votingSessionService;

    public AgendaService(AgendaRepository agendaRepository, VotingSessionService votingSessionService) {
        this.agendaRepository = agendaRepository;
        this.votingSessionService = votingSessionService;
    }

    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public Agenda findById(String id) {
        return agendaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public Collection<Agenda> findAll() {
        return agendaRepository.findAll();
    }

    public void delete(String id) {
        agendaRepository.deleteById(id);
        votingSessionService.deleteByAgendaId(id);
    }

    public void update(String id, Agenda agendaUpdate) {
        agendaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
        if (votingSessionService.existsByAgendaId(id)) {
            throw new BadRequestException("Unable to update agenda that with voting session started.");
        }
        agendaUpdate.setId(id);
        agendaRepository.save(agendaUpdate);
    }
}
