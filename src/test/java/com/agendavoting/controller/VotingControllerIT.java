package com.agendavoting.controller;

import com.agendavoting.enums.Status;
import com.agendavoting.enums.VotingOption;
import com.agendavoting.model.Agenda;
import com.agendavoting.repository.AgendaRepository;
import com.agendavoting.repository.VoteRepository;
import com.agendavoting.repository.VotingSessionRepository;
import com.agendavoting.restClient.CPFConsultationClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class VotingControllerIT {

    @Container
    static final MongoDBContainer mongo =
            new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.data.mongodb.uri",
                mongo::getReplicaSetUrl
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VotingSessionRepository votingSessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @MockitoBean
    private CPFConsultationClient cpfConsultationClient;

    private static final String CPF = "91693816075";

    @BeforeEach
    void cleanDatabase() {
        voteRepository.deleteAll();
        votingSessionRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    void shouldVoteSuccessfully() throws Exception {

        String agendaRequest = """
                {
                    "title": "Voting integration test",
                    "description": "Testing the complete voting flow"
                }
                """;

        mockMvc.perform(post("/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(agendaRequest))
                .andExpect(status().is2xxSuccessful());

        Agenda agenda = agendaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        String votingSessionRequest = """
                {
                    "agendaId": "%s",
                    "timeDuration": 5
                }
                """.formatted(agenda.getId());

        mockMvc.perform(post("/votingSession/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(votingSessionRequest))
                .andExpect(status().isCreated());


        when(cpfConsultationClient.getStatus(CPF))
                .thenReturn(Status.ABLE_TO_VOTE);

        String voteRequest = """
                {
                    "agendaId": "%s",
                    "cpf": "%s",
                    "votingOption": "YES"
                }
                """.formatted(agenda.getId(), CPF);

        mockMvc.perform(post("/voting/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(voteRequest))
                .andExpect(status().is2xxSuccessful());

        assertEquals(
                1,
                voteRepository.countByVotingSessionIdAndVotingOption(
                        votingSessionRepository
                                .findByAgendaId(agenda.getId())
                                .orElseThrow()
                                .getId(),
                        VotingOption.YES
                )
        );
    }
}