package com.votingAgenda.controller;

import com.votingAgenda.DTO.VoteDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import com.votingAgenda.exception.ErrorResponseWithFields;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/voting")
@RestController
@Tag(name = "Voting", description = "Operations about Vote")
public class VotingController {
    private final SessionVoteBusiness sessionVoteBusiness;
    private final ModelMapper defaultModelMapper;

    public VotingController(SessionVoteBusiness sessionVoteBusiness, ModelMapper defaultModelMapper) {
        this.sessionVoteBusiness = sessionVoteBusiness;
        this.defaultModelMapper = defaultModelMapper;
    }

    @Operation(summary = "Vote", description = "Vote on an agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Some required attribute is missing",
                    content = @Content(schema = @Schema(implementation = ErrorResponseWithFields.class)))
    })
    @PostMapping(path = "/vote", consumes = "application/json")
    public ResponseEntity createSession(@Validated @RequestBody VoteDTO voteDTO) {
        sessionVoteBusiness.toVote(voteDTO);
        return ResponseEntity.ok().build();
    }

}
