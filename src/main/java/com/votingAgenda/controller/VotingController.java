package com.votingAgenda.controller;

import com.votingAgenda.DTO.VoteDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/voting")
@RestController
public class VotingController {
    private SessionVoteBusiness sessionVoteBusiness;
    private ModelMapper defaultModelMapper;

    public VotingController(SessionVoteBusiness sessionVoteBusiness, ModelMapper defaultModelMapper) {
        this.sessionVoteBusiness = sessionVoteBusiness;
        this.defaultModelMapper = defaultModelMapper;
    }

    @PostMapping("/vote")
    public ResponseEntity createSession(@Validated  @RequestBody VoteDTO voteDTO,  BindingResult errors) throws Exception {
       sessionVoteBusiness.toVote(voteDTO);
        return ResponseEntity.ok().build();
    }

}
