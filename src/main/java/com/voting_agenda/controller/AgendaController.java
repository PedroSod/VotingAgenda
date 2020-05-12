package com.voting_agenda.controller;

import com.voting_agenda.DTO.AgendaDTO;
import com.voting_agenda.model.Agenda;
import com.voting_agenda.service.AgendaService;
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
    private AgendaService agendaService;
    private ModelMapper defaultModelMapper;

    public AgendaController(AgendaService agendaService, ModelMapper defaultModelMapper) {
        this.agendaService = agendaService;
        this.defaultModelMapper = defaultModelMapper;
    }

    @PostMapping
    public ResponseEntity criarPauta(@Validated @RequestBody AgendaDTO agendaDTO) {
        Agenda agenda = defaultModelMapper.map(agendaDTO, Agenda.class);
        String id = agendaService.save(agenda).getId();
        URI location = UriComponentsBuilder.fromUriString("agenda")
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AgendaDTO> getPautaById(@PathVariable String id) {
        AgendaDTO agendaDTO = defaultModelMapper.map(agendaService.findById(id), AgendaDTO.class);
        return ResponseEntity.ok(agendaDTO);
    }

    @GetMapping
    public ResponseEntity<Collection<AgendaDTO>> getAll() {
        Collection<Agenda> todasPautas = agendaService.findAll();
        Collection<AgendaDTO> todasPautasDTO = todasPautas.stream().map(
                agenda -> defaultModelMapper.map(agenda, AgendaDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(todasPautasDTO);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Validated @RequestBody AgendaDTO agendaDTO) {
        Agenda agenda = defaultModelMapper.map(agendaDTO, Agenda.class);
        agendaService.update(id, agenda);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        agendaService.delete(id);
    }
}
