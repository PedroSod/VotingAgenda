package com.agendavoting.restClient;

import com.agendavoting.dto.CPFConsultDTO;
import com.agendavoting.enums.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class CPFConsultationClient {
    private final RestClient restClient;

    public CPFConsultationClient(RestClient.Builder restClientBuilder, @Value("${cpf-consult.url}") URI apiUrl) {
        this.restClient = restClientBuilder.baseUrl(apiUrl.toString()).build();
    }
    public Status getStatus(String cpf) {
        CPFConsultDTO response = restClient.get().uri("{cpf}", cpf).retrieve().body(CPFConsultDTO.class);
        if (response == null || response.status() == null) {
            throw new IllegalStateException("CPF consultation returned an invalid response");
        }
        return response.status();
    }
}
