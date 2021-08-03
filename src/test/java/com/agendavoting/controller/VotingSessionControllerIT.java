package com.agendavoting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.agendavoting.DTO.VotingSessionInputDTO;
import com.agendavoting.business.SessionVoteBusiness;
import com.agendavoting.configuration.ApplicationConfig;
import com.agendavoting.exception.ExistingSessionException;
import com.agendavoting.exception.RecordNotFoundException;
import com.agendavoting.model.Agenda;
import com.agendavoting.model.VotingSession;
import com.agendavoting.service.VotingSessionService;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
    private static final String TEST_ID = "testId";
    private static final LocalDateTime date = LocalDateTime.now();
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
    public void sessionAlreadyExistTest() throws Exception {
        VotingSessionInputDTO votingSessionInputDTO = generateVotingSessionInputDTO();
        doThrow(new ExistingSessionException())
                .when(sessionVoteBusiness).startVotingSession(eq(votingSessionInputDTO));
        mockMvc.perform(
                post("/votingSession/start")
                        .content(mapper.writeValueAsString(votingSessionInputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("There is already a session for this agenda."));


    }


    @Test
    public void createSessionRecordNotFoundExceptionTest() throws Exception {
        VotingSessionInputDTO votingSessionInputDTO = generateVotingSessionInputDTO();

        doThrow(new RecordNotFoundException(votingSessionInputDTO.getAgendaId()))
                .when(sessionVoteBusiness).startVotingSession(eq(votingSessionInputDTO));

        mockMvc.perform(
                post("/votingSession/start")
                        .content(mapper.writeValueAsString(votingSessionInputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("No record found for id : " + votingSessionInputDTO.getAgendaId()));
    }

    @Test
    public void getVotingSessionRecordNotFoundExceptionTest() throws Exception {

        doThrow(new RecordNotFoundException(TEST_ID))
                .when(votingSessionService).findById(eq(TEST_ID));

        mockMvc.perform(get((String.format("%s/%s", "/votingSession", TEST_ID))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("No record found for id : " + TEST_ID));
    }

    @Test
    public void getVotingSessionById() throws Exception {
        VotingSession votingSession = generateVotingSession();

        when(votingSessionService.findById(TEST_ID)).thenReturn(votingSession);
        mockMvc.perform(get((String.format("%s/%s", "/votingSession", votingSession.getId()))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(votingSession.getId()))
                .andExpect(jsonPath("$.agenda.id").value(votingSession.getAgenda().getId()))
                .andExpect(jsonPath("$.agenda.title").value(votingSession.getAgenda().getTitle()))
                .andExpect(jsonPath("$.agenda.description").value(votingSession.getAgenda().getDescription()))
                .andExpect(jsonPath("$.start").value(votingSession.getStart().toString()))
                .andExpect(jsonPath("$.end").value(votingSession.getEnd().toString()));

    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete(String.format("%s/%s", "/votingSession", TEST_ID))).andExpect(status().is2xxSuccessful());

    }

    public static VotingSessionInputDTO generateVotingSessionInputDTO() {
        return new VotingSessionInputDTO().builder()
                .agendaId(TEST_ID)
                .start(LocalDateTime.now())
                .timeDuration(60L)
                .build();
    }

    private static VotingSession generateVotingSession() {
        return new VotingSession().builder()
                .id(TEST_ID)
                .agenda(generateAgenda())
                .start(date)
                .end(date.plusMinutes(60L)).build();
    }

    private static Agenda generateAgenda() {
        return new Agenda().builder()
                .id(TEST_ID)
                .title("testTitle")
                .description("test description")
                .build();
    }
}