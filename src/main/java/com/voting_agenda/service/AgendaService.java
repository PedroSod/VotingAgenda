package com.voting_agenda.service;

import com.voting_agenda.exception.BadRequestException;
import com.voting_agenda.exception.RecordNotFoundException;
import com.voting_agenda.model.Agenda;
import com.voting_agenda.repository.AgendaRepository;
import com.voting_agenda.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final VotingSessionRepository votingSessionRepository;

    public AgendaService(AgendaRepository agendaRepository, VotingSessionRepository votingSessionRepository) {
        this.agendaRepository = agendaRepository;
        this.votingSessionRepository = votingSessionRepository;
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
        votingSessionRepository.deleteByAgendaId(id);
    }

    public void update(String id, Agenda agendaUpdate) {
        agendaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
        votingSessionRepository.findByAgenda(agendaUpdate).
                orElseThrow(() ->
                        new BadRequestException("Unable to update agenda that with voting session started."));
        agendaUpdate.setId(id);
        agendaRepository.save(agendaUpdate);
    }
}
