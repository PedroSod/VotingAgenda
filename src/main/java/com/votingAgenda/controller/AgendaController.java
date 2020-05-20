package com.votingAgenda.controller;

import com.votingAgenda.DTO.AgendaInputDTO;
import com.votingAgenda.DTO.AgendaOutputDTO;
import com.votingAgenda.DTO.VotingResultDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.service.AgendaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping("/agenda")
@RestController
public class AgendaController {
    private final AgendaService agendaService;
    private final SessionVoteBusiness sessionVoteBusiness;
    private final ModelMapper defaultModelMapper;

    public AgendaController(AgendaService agendaService, SessionVoteBusiness sessionVoteBusiness, ModelMapper defaultModelMapper) {
        this.agendaService = agendaService;
        this.sessionVoteBusiness = sessionVoteBusiness;
        this.defaultModelMapper = defaultModelMapper;
    }

    @PostMapping
    public ResponseEntity createAgenda(@Validated @RequestBody AgendaInputDTO agendaDTO) {
        Agenda agenda = defaultModelMapper.map(agendaDTO, Agenda.class);
        String id = agendaService.save(agenda).getId();
        URI location = UriComponentsBuilder.fromUriString("agenda")
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaInputDTO> getAgendaById(@PathVariable String id) {
        AgendaOutputDTO agendaDTO = defaultModelMapper.map(agendaService.findById(id), AgendaOutputDTO.class);
        return ResponseEntity.ok(agendaDTO);
    }

    @GetMapping
    public ResponseEntity<Collection<AgendaOutputDTO>> getAll() {
        Collection<Agenda> todasPautas = agendaService.findAll();
        Collection<AgendaOutputDTO> todasPautasDTO = todasPautas.stream().map(
                agenda -> defaultModelMapper.map(agenda, AgendaOutputDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(todasPautasDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Validated @RequestBody AgendaInputDTO agendaDTO) {
        Agenda agenda = defaultModelMapper.map(agendaDTO, Agenda.class);
        agendaService.update(id, agenda);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        agendaService.delete(id);
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<VotingResultDTO> getResult(@PathVariable String id) {
        return ResponseEntity.ok(sessionVoteBusiness.getVotingResult(id));
    }
}
