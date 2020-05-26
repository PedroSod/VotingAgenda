package com.agendavoting.controller;

import com.agendavoting.DTO.AgendaInputDTO;
import com.agendavoting.DTO.AgendaOutputDTO;
import com.agendavoting.DTO.VotingResultDTO;
import com.agendavoting.business.SessionVoteBusiness;
import com.agendavoting.exception.ErrorResponse;
import com.agendavoting.exception.ErrorResponseWithFields;
import com.agendavoting.model.Agenda;
import com.agendavoting.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Agenda", description = "Operations about Agenda")
public class AgendaController {
    private final AgendaService agendaService;
    private final SessionVoteBusiness sessionVoteBusiness;
    private final ModelMapper defaultModelMapper;

    public AgendaController(AgendaService agendaService, SessionVoteBusiness sessionVoteBusiness, ModelMapper defaultModelMapper) {
        this.agendaService = agendaService;
        this.sessionVoteBusiness = sessionVoteBusiness;
        this.defaultModelMapper = defaultModelMapper;
    }

    @Operation(summary = "Create Agenda", description = "Get a Agenda by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Some required attribute is missing",
                    content = @Content(schema = @Schema(implementation = ErrorResponseWithFields.class)))
    })
    @PostMapping(consumes = "application/json")
    public ResponseEntity createAgenda(@Validated @RequestBody AgendaInputDTO agendaDTO) {
        Agenda agenda = defaultModelMapper.map(agendaDTO, Agenda.class);
        String id = agendaService.save(agenda).getId();
        URI location = UriComponentsBuilder.fromUriString("agenda")
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Get Agenda", description = "Get a Agenda by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AgendaInputDTO.class))),
            @ApiResponse(responseCode = "404", description = "Agenda not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<AgendaInputDTO> getAgendaById(@PathVariable String id) {
        AgendaOutputDTO agendaDTO = defaultModelMapper.map(agendaService.findById(id), AgendaOutputDTO.class);
        return ResponseEntity.ok(agendaDTO);
    }

    @Operation(summary = "Get All Agendas", description = "Get all agendas from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendaOutputDTO.class)))),
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Collection<AgendaOutputDTO>> getAll() {
        Collection<Agenda> allAgendas = agendaService.findAll();
        Collection<AgendaOutputDTO> allAgendasDTO = allAgendas.stream().map(
                agenda -> defaultModelMapper.map(agenda, AgendaOutputDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(allAgendasDTO);
    }

    @Operation(summary = "Update Agenda", description = "Get a Agenda by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Agenda not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Some required attribute is missing",
                    content = @Content(schema = @Schema(implementation = ErrorResponseWithFields.class)))
    })
    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Validated @RequestBody AgendaInputDTO agendaDTO) {
        Agenda agenda = defaultModelMapper.map(agendaDTO, Agenda.class);
        agendaService.update(id, agenda);
    }

    @Operation(summary = "Delete by id", description = "Delete Agenda by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "successful operation")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        agendaService.delete(id);
    }

    @Operation(summary = "Get Agenda Result", description = "Get a Agenda voting result by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agenda created",
                    content = @Content(schema = @Schema(implementation = VotingResultDTO.class))),
            @ApiResponse(responseCode = "404", description = "Agenda not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/{id}/result", produces = "application/json")
    public ResponseEntity<VotingResultDTO> getResult(@PathVariable String id) {
        return ResponseEntity.ok(sessionVoteBusiness.getVotingResult(id));
    }
}
