package com.votingAgenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.votingAgenda.DTO.AgendaInputDTO;
import com.votingAgenda.DTO.VotingResultDTO;
import com.votingAgenda.business.SessionVoteBusiness;
import com.votingAgenda.configuration.ApplicationConfig;
import com.votingAgenda.exception.BadRequestException;
import com.votingAgenda.model.Agenda;
import com.votingAgenda.service.AgendaService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AgendaController.class)
@Import(value = {ApplicationConfig.class, RestTemplateAutoConfiguration.class})
public class AgendaControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper;
    @MockBean
    private AgendaService agendaService;
    @MockBean
    private SessionVoteBusiness sessionVoteBusiness;
    private static final String TEST_ID = "testId";

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void createAgendaTest() throws Exception {
        Agenda agenda = generateAgenda();
        when(agendaService.save(any(Agenda.class))).thenReturn(agenda);
        mockMvc.perform(post("/agenda")
                .content(mapper.writeValueAsString(agenda))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createAgendaBadRequestTest() throws Exception {
        Agenda agenda = generateAgenda();
        agenda.setTitle("");
        mockMvc.perform(post("/agenda")
                .content(mapper.writeValueAsString(agenda))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.fieldErrors[0]").value("title - must not be blank"));
    }
    @Test
    public void getAgendaByIdTest() throws Exception {
        Agenda agenda = generateAgenda();
        when(agendaService.findById(TEST_ID)).thenReturn(agenda);

        mockMvc.perform(get((String.format("%s/%s", "/agenda", TEST_ID))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(agenda.getId()))
                .andExpect(jsonPath("$.title").value(agenda.getTitle()))
                .andExpect(jsonPath("$.description").value(agenda.getDescription()));

    }

    @Test
    public void getAllTest() throws Exception {
        Agenda agenda = generateAgenda();
        when(agendaService.findAll()).thenReturn(Collections.singletonList(agenda));

        mockMvc.perform(get(("/agenda")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].id").value(agenda.getId()))
                .andExpect(jsonPath("$[0].title").value(agenda.getTitle()))
                .andExpect(jsonPath("$[0].description").value(agenda.getDescription()));
    }

    @Test
    public void updateTest() throws Exception {
        Agenda agenda = generateAgenda();
        mockMvc.perform(put((String.format("%s/%s", "/agenda", TEST_ID)))
                .content(mapper.writeValueAsString(agenda))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateBadRequestExceptionTest() throws Exception {
        Agenda agenda = generateAgenda();
        agenda.setId(null);
        doThrow(new BadRequestException("Unable to update agenda that with voting session started."))
                .when(agendaService).update(eq(TEST_ID), eq(agenda));

        mockMvc.perform(put((String.format("%s/%s", "/agenda", TEST_ID)))
                .content(mapper.writeValueAsString(agenda))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Unable to update agenda that with voting session started."));

    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete((String.format("%s/%s", "/agenda", TEST_ID))))
                .andExpect(status().is2xxSuccessful());


    }

    @Test
    public void getResultTest() throws Exception {
        VotingResultDTO votingResultDTO = generateVotingResultDTO();
        when(sessionVoteBusiness.getVotingResult(TEST_ID)).thenReturn(votingResultDTO);

        mockMvc.perform(get((String.format("%s/%s/%s", "/agenda", TEST_ID, "result"))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.yesVotes").value(votingResultDTO.getYesVotes()))
                .andExpect(jsonPath("$.noVotes").value(votingResultDTO.getNoVotes()))
                .andExpect(jsonPath("$.totalVotes").value(votingResultDTO.getTotalVotes()));
    }

    private static AgendaInputDTO generateAgendaInputDTO() {
        return new AgendaInputDTO().builder()
                .title("testTitle")
                .description("test description")
                .build();
    }

    private static Agenda generateAgenda() {
        return new Agenda().builder()
                .id(TEST_ID)
                .title("testTitle")
                .description("test description")
                .build();
    }

    private static VotingResultDTO generateVotingResultDTO() {
        return new VotingResultDTO()
                .builder()
                .yesVotes(1L)
                .noVotes(2L)
                .totalVotes(3L)
                .build();
    }
}