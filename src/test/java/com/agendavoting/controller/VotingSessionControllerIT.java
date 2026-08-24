package com.agendavoting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.agendavoting.dto.VotingSessionInputDTO;
import com.agendavoting.model.Agenda;
import com.agendavoting.model.VotingSession;
import com.agendavoting.repository.AgendaRepository;
import com.agendavoting.repository.VotingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class VotingSessionControllerIT {

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

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String AGENDA_ID = "agenda-integration-test";

    @BeforeEach
    void cleanDatabase() {
        votingSessionRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    void shouldCreateVotingSession() throws Exception {

        // Arrange
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .title("Agenda integration test")
                .description("Testing voting session creation")
                .build();

        agendaRepository.save(agenda);

        VotingSessionInputDTO votingSessionRequest =
                new VotingSessionInputDTO(
                        AGENDA_ID,
                        LocalDateTime.now(),
                        60L
                );

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                false
        );

        // Act
        mockMvc.perform(post("/votingSession/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(votingSessionRequest)))
                .andExpect(status().isCreated());

        // Assert
        VotingSession savedSession =
                votingSessionRepository.findByAgendaId(AGENDA_ID)
                        .orElseThrow();

        assertNotNull(savedSession.getId());
        assertEquals(AGENDA_ID, savedSession.getAgenda().getId());
        assertEquals(60,
                java.time.Duration.between(
                        savedSession.getStart(),
                        savedSession.getEnd()
                ).toMinutes()
        );
    }

    @Test
    void shouldGetVotingSessionById() throws Exception {

        // Arrange
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .title("Agenda integration test")
                .description("Testing voting session retrieval")
                .build();

        agendaRepository.save(agenda);

        VotingSession session = VotingSession.builder()
                .agenda(agenda)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(60))
                .build();

        VotingSession savedSession =
                votingSessionRepository.save(session);

        // Act + Assert
        mockMvc.perform(
                        get("/votingSession/{id}", savedSession.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(savedSession.getId()))
                .andExpect(jsonPath("$.agenda.id")
                        .value(AGENDA_ID))
                .andExpect(jsonPath("$.agenda.title")
                        .value("Agenda integration test"))
                .andExpect(jsonPath("$.agenda.description")
                        .value("Testing voting session retrieval"))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists());
    }

    @Test
    void shouldDeleteVotingSession() throws Exception {

        // Arrange
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .title("Agenda integration test")
                .description("Testing voting session deletion")
                .build();

        agendaRepository.save(agenda);

        VotingSession session = VotingSession.builder()
                .agenda(agenda)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(60))
                .build();

        VotingSession savedSession =
                votingSessionRepository.save(session);

        // Act
        mockMvc.perform(
                        delete("/votingSession/{id}", savedSession.getId())
                )
                .andExpect(status().isNoContent());

        // Assert
        assertFalse(votingSessionRepository.existsById(savedSession.getId()));
    }
}