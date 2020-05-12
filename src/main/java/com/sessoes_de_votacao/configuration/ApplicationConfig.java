package com.sessoes_de_votacao.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfig {

    private static final Integer CONNECTION_TIMEOUT = 25;

    @Bean
    public ModelMapper defaultModelMapper() {
        return new ModelMapper();
    }
}
