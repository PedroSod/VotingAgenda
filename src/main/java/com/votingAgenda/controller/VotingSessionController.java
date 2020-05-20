package com.votingAgenda.controller;

import com.votingAgenda.DTO.VotingSessionInputDTO;
import com.votingAgenda.DTO.VotingSessionOutputDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import com.votingAgenda.service.VotingSessionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequestMapping("/votingSession")
@RestController
public class VotingSessionController {
    private final ModelMapper defaultModelMapper;
    private final VotingSessionService votingSessionService;
    private final SessionVoteBusiness sessionVoteBusiness;

    public VotingSessionController( ModelMapper defaultModelMapper, VotingSessionService votingSessionService, SessionVoteBusiness sessionVoteBusiness) {
        this.defaultModelMapper = defaultModelMapper;
        this.votingSessionService = votingSessionService;
        this.sessionVoteBusiness = sessionVoteBusiness;
    }

    @PostMapping("/start")
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
