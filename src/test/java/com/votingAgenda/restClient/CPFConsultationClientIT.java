package com.votingAgenda.restClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.votingAgenda.DTO.CPFConsultDTO;
import com.votingAgenda.configuration.ApplicationConfig;
import com.votingAgenda.enums.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@RestClientTest(CPFConsultationClient.class)
@Import(ApplicationConfig.class)
public class CPFConsultationClientIT {

    @Autowired
    private CPFConsultationClient cpfConsultationClient;

    @Autowired
    private MockRestServiceServer server;

    private static ObjectMapper objectMapper;
    private static String endPoint;
    private static String cpf;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        endPoint = "https://user-info.herokuapp.com/users/";
        cpf = "19839091069";
    }


    @Test
    public void getStatus() throws JsonProcessingException {
        CPFConsultDTO cpfConsultDTO = new CPFConsultDTO(Status.ABLE_TO_VOTE);
        String url = UriComponentsBuilder.fromUriString(endPoint)
                .path("{cpf}").buildAndExpand(cpf).toUriString();

        server.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(cpfConsultDTO), MediaType.APPLICATION_JSON));

        Status status = cpfConsultationClient.getStatus(cpf);
        server.verify();
        assertEquals(cpfConsultDTO.getStatus(), status);
    }
}