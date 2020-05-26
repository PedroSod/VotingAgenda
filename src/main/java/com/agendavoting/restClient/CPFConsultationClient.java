package com.agendavoting.restClient;

import com.agendavoting.DTO.CPFConsultDTO;
import com.agendavoting.enums.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class CPFConsultationClient {
    private final RestTemplate restTemplate;
    private final URI apiUrl;

    public CPFConsultationClient(RestTemplate restTemplate, @Value("${cpf-consult.url}") URI apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }
    public Status getStatus(String cpf) {
        String url = UriComponentsBuilder.fromUri(apiUrl).path("{cpf}").buildAndExpand(cpf).toUriString();
        return restTemplate.getForObject(url, CPFConsultDTO.class).getStatus();
    }
}
