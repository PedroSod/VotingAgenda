package com.agendavoting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.agendavoting.DTO.VoteDTO;
import com.agendavoting.business.SessionVoteBusiness;
import com.agendavoting.configuration.ApplicationConfig;
import com.agendavoting.enums.VotingOption;
import com.agendavoting.exception.DuplicateVoteException;
import com.agendavoting.exception.UnableToVoteException;
import com.agendavoting.exception.VotingClosedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VotingController.class)
@Import(value = {ApplicationConfig.class, RestTemplateAutoConfiguration.class})
public class VotingControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private SessionVoteBusiness sessionVoteBusiness;
    private static final String TEST_ID = "testId";


    @Test
    public void toVoteTest() throws Exception {
        VoteDTO voteDTO = generateVoteDTO();
        doNothing().when(sessionVoteBusiness).toVote(eq(voteDTO));
        mockMvc.perform(post("/voting/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(voteDTO)))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void toVoteVoteOptionBadRequestTest() throws Exception {
        VoteDTO voteDTO = generateVoteDTO();
        voteDTO.setVotingOption(null);
        doNothing().when(sessionVoteBusiness).toVote(eq(voteDTO));
        mockMvc.perform(post("/voting/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(voteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.fieldErrors[0]").value(
                        "votingOption - must not be null"));

    }

    @Test
    public void toVoteVoteOptionTypeBadRequestTest() throws Exception {
        VoteDTO voteDTO = generateVoteDTO();
        String payload = mapper.writeValueAsString(voteDTO).replace("YES", "");
        doNothing().when(sessionVoteBusiness).toVote(eq(voteDTO));
        mockMvc.perform(post("/voting/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value("InvalidFormatException: Cannot deserialize value of type `com.agendavoting.enums.VotingOption` from String \"\": not one of the values accepted for Enum class: [NO, YES]"));
    }

    @Test
    public void toVoteUnableToVoteExceptionTest() throws Exception {
        VoteDTO voteDTO = generateVoteDTO();

        doThrow(new UnableToVoteException(voteDTO.getCpf()))
                .when(sessionVoteBusiness).toVote(generateVoteDTO());

        mockMvc.perform(post("/voting/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(voteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("The cpf " + voteDTO.getCpf() + " is unable to vote."));
    }

    @Test
    public void toVoteDuplicateVoteExceptionTest() throws Exception {
        VoteDTO voteDTO = generateVoteDTO();

        doThrow(new DuplicateVoteException(voteDTO.getCpf()))
                .when(sessionVoteBusiness).toVote(generateVoteDTO());

        mockMvc.perform(post("/voting/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(voteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("the CPF " + voteDTO.getCpf() + ", has already voted on this agenda."));
    }

    @Test
    public void toVoteVotingClosedExceptionTest() throws Exception {
        VoteDTO voteDTO = generateVoteDTO();

        doThrow(new VotingClosedException(voteDTO.getAgendaId()))
                .when(sessionVoteBusiness).toVote(generateVoteDTO());

        mockMvc.perform(post("/voting/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(voteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("The voting session " + voteDTO.getAgendaId() + " has ended."));
    }

    public static VoteDTO generateVoteDTO() {
        return new VoteDTO().builder()
                .agendaId("testId")
                .cpf("91693816075")
                .votingOption(VotingOption.YES)
                .build();
    }
}