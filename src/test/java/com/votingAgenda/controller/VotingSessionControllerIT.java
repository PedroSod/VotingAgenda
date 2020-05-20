package com.votingAgenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.votingAgenda.DTO.VotingSessionInputDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import com.votingAgenda.configuration.ApplicationConfig;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.model.VotingSession;
import com.votingAgenda.service.VotingSessionService;
import org.junit.jupiter.api.BeforeAll;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VotingSessionController.class)
@Import(value = {ApplicationConfig.class, RestTemplateAutoConfiguration.class})
public class VotingSessionControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper;
    @MockBean
    private VotingSessionService votingSessionService;
    @MockBean
    private SessionVoteBusiness sessionVoteBusiness;
    private static String TEST_ID = "testId";

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void createSession() throws Exception {
        VotingSessionInputDTO votingSessionInputDTO = generateVotingSessionInputDTO();
        when(sessionVoteBusiness.startVotingSession(eq(votingSessionInputDTO)))
                .thenReturn(TEST_ID);
        mockMvc.perform(
                post("/votingSession/start")
                        .content(mapper.writeValueAsString(votingSessionInputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getVotingSessionById() throws Exception {
        VotingSession votingSession = generateVotingSession();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        when(votingSessionService.findById(TEST_ID)).thenReturn(votingSession);
        mockMvc.perform(get((String.format("%s/%s", "/votingSession", votingSession.getId()))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(votingSession.getId()))
                .andExpect(jsonPath("$.agenda.id").value(votingSession.getAgenda().getId()))
                .andExpect(jsonPath("$.agenda.title").value(votingSession.getAgenda().getTitle()))
                .andExpect(jsonPath("$.agenda.description").value(votingSession.getAgenda().getDescription()))
                .andExpect(jsonPath("$.start").value(votingSession.getStart().format(dateTimeFormatter)))
                .andExpect(jsonPath("$.end").value(votingSession.getEnd().format(dateTimeFormatter)));


    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete(String.format("%s/%s", "/votingSession", TEST_ID))).andExpect(status().is2xxSuccessful());

    }
    public static VotingSessionInputDTO generateVotingSessionInputDTO() {
        return new VotingSessionInputDTO().builder()
                .agendaId(TEST_ID)
                .start(ZonedDateTime.now(ZoneId.of(("UTC"))))
                .timeDuration(60L)
                .build();
    }

    private static VotingSession generateVotingSession() {
        return new VotingSession().builder()
                .id(TEST_ID)
                .agenda(generateAgenda())
                .start(ZonedDateTime.now(ZoneId.of(("UTC"))))
                .end(ZonedDateTime.now(ZoneId.of(("UTC"))).plusMinutes(60L)).build();
    }

    private static Agenda generateAgenda() {
        return new Agenda().builder()
                .id(TEST_ID)
                .title("testTitle")
                .description("test description")
                .build();
    }
}