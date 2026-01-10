package com.nims.patient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

//@Configuration
public class AppConfig {

    @Bean
    public RestClient authRestClient(@Value("${auth.service.base-url}") String authBaseUrl) {

        return RestClient.builder()
                .baseUrl(authBaseUrl)
                .build();
    }
}
