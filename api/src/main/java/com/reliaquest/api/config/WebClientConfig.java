package com.reliaquest.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient employeeWebClient(
            @Value("${api.url:http://localhost:8112/api/v1/employee}") String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }
}
