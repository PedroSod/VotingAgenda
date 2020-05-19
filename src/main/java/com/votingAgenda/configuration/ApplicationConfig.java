package com.votingAgenda.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ApplicationConfig {

    private static final Integer CONNECTION_TIMEOUT = 25;

    @Bean
    public ModelMapper defaultModelMapper() {
        return new ModelMapper();
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
