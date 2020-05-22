package com.votingAgenda.controller;

import com.votingAgenda.DTO.VotingSessionInputDTO;
import com.votingAgenda.DTO.VotingSessionOutputDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import com.votingAgenda.exception.ErrorResponse;
import com.votingAgenda.exception.ErrorResponseWithFields;
import com.votingAgenda.service.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequestMapping("/votingSession")
@RestController
@Tag(name = "Voting Session", description = "Operations about Voting Session")
public class VotingSessionController {
    private final ModelMapper defaultModelMapper;
    private final VotingSessionService votingSessionService;
    private final SessionVoteBusiness sessionVoteBusiness;

    public VotingSessionController(ModelMapper defaultModelMapper, VotingSessionService votingSessionService, SessionVoteBusiness sessionVoteBusiness) {
        this.defaultModelMapper = defaultModelMapper;
        this.votingSessionService = votingSessionService;
        this.sessionVoteBusiness = sessionVoteBusiness;
    }

    @Operation(summary = "Create Voting Session", description = "Create a Voting Session to an Agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voting Session created"),
            @ApiResponse(responseCode = "400", description = "Some required attribute is missing",
                    content = @Content(schema = @Schema(implementation = ErrorResponseWithFields.class)))
    })
    @PostMapping(path = "/start", consumes = "application/json")
    public ResponseEntity createSession(@RequestBody VotingSessionInputDTO votingSessionDTO) {
        String id = sessionVoteBusiness.startVotingSession(votingSessionDTO);
        URI location = UriComponentsBuilder.fromUriString("votingSession")
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Get Voting Session", description = "Get a Voting Session by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = VotingSessionOutputDTO.class))),
            @ApiResponse(responseCode = "404", description = "Voting Session not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<VotingSessionOutputDTO> getVotingSessionById(@PathVariable String id) {
        VotingSessionOutputDTO votingSessionDTO =
                defaultModelMapper.map(votingSessionService.findById(id), VotingSessionOutputDTO.class);
        return ResponseEntity.ok(votingSessionDTO);
    }

    @Operation(summary = "Delete by id", description = "Delete Voting Session by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "successful operation")
    })
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        votingSessionService.delete(id);
    }
}
