package com.votingAgenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.votingAgenda.DTO.VoteDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import com.votingAgenda.configuration.ApplicationConfig;
import com.votingAgenda.enums.VotingOption;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VotingController.class)
@Import(value = { ApplicationConfig.class, RestTemplateAutoConfiguration.class})
public class VotingControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private SessionVoteBusiness sessionVoteBusiness;

    private static final String anyString = "any_string";


    @Test
    public void createSession() throws Exception {
        VoteDTO voteDTO = generateVoteDTO();
        doNothing().when(sessionVoteBusiness).toVote(eq(voteDTO));
        mockMvc.perform(post("/voting/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(voteDTO)))
                .andExpect(status().is2xxSuccessful());

    }

    public static VoteDTO generateVoteDTO() {
        return new VoteDTO().builder()
                .agendaId("testId")
                .cpf("91693816075")
                .votingOption(VotingOption.YES)
                .build();
    }
}