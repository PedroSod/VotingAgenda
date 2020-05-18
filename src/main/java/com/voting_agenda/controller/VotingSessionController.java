package com.voting_agenda.controller;

import com.voting_agenda.DTO.VotingSessionInputDTO;
import com.voting_agenda.DTO.VotingSessionOutputDTO;
import com.voting_agenda.business.SessionVoteBusiness;
import com.voting_agenda.service.AgendaService;
import com.voting_agenda.service.VotingSessionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequestMapping("/votingSession")
@RestController
public class VotingSessionController {
    private ModelMapper defaultModelMapper;
    private VotingSessionService votingSessionService;
    private SessionVoteBusiness sessionVoteBusiness;

    public VotingSessionController( ModelMapper defaultModelMapper, VotingSessionService votingSessionService, SessionVoteBusiness sessionVoteBusiness) {
        this.defaultModelMapper = defaultModelMapper;
        this.votingSessionService = votingSessionService;
        this.sessionVoteBusiness = sessionVoteBusiness;
    }

    @PostMapping("/start/")
    public ResponseEntity createSession(@RequestBody VotingSessionInputDTO votingSessionDTO) {
        String id = sessionVoteBusiness.startVotingSession(votingSessionDTO);
        URI location = UriComponentsBuilder.fromUriString("votingSession")
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<VotingSessionOutputDTO> getVotingSessionById(@PathVariable String id) {
        VotingSessionOutputDTO votingSessionDTO =
                defaultModelMapper.map(votingSessionService.findById(id), VotingSessionOutputDTO.class);
        return ResponseEntity.ok(votingSessionDTO);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        votingSessionService.delete(id);
    }
}
