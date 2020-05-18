package com.voting_agenda.service;

import com.voting_agenda.exception.BadRequestException;
import com.voting_agenda.exception.RecordNotFoundException;
import com.voting_agenda.model.Agenda;
import com.voting_agenda.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final VotingSessionService votingSessionService;

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
