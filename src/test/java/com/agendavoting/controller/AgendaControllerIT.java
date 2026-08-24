package com.agendavoting.controller;

import com.agendavoting.repository.AgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class AgendaControllerIT {

    @Container
    static final MongoDBContainer mongo =
            new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @Autowired
    private AgendaRepository agendaRepository;

    @BeforeEach
    void cleanDatabase() {
        agendaRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveAgenda() throws Exception {

        String request = """
                {
                    "title": "Agenda integration test",
                    "description": "Testing the complete flow"
                }
                """;

        // Controller -> Service -> Repository -> MongoDB
        mockMvc.perform(post("/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is2xxSuccessful());

        // MongoDB -> Repository -> Service -> Controller -> JSON
        mockMvc.perform(get("/agenda"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].title")
                        .value("Agenda integration test"))
                .andExpect(jsonPath("$[0].description")
                        .value("Testing the complete flow"));
    }
}