package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Value("${brevo.key}")
    private String brevoApiKey;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean(name = "brevoWebClient")
    public WebClient brevoWebClient() {
        return WebClient.builder()
                .defaultHeader("api-key", brevoApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Bean(name = "orderRestClient")
    public RestClient orderRestClient() {
        return RestClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }
}
