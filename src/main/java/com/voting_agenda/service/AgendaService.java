package com.voting_agenda.service;

import com.voting_agenda.model.Agenda;
import com.voting_agenda.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AgendaService {

    private AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public Agenda findById(String id) {
        return agendaRepository.findById(id).orElseThrow();
    }

    public Collection<Agenda> findAll() {
        return agendaRepository.findAll();
    }

    public void delete(String id) {
        agendaRepository.deleteById(id);
    }

    public void update(String id, Agenda agendaUpdate) {
        agendaRepository.findById(id).orElseThrow();
        agendaUpdate.setId(id);
        agendaRepository.save(agendaUpdate);
    }
}
