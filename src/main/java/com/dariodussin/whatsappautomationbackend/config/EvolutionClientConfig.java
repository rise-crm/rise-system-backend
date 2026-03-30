package com.dariodussin.whatsappautomationbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EvolutionClientConfig {
    @Value("${evolution.url}")
    private String evolutionUrl;

    @Value("${evolution.key}")
    private String evolutionKey;

    @Bean
    public WebClient evolutionClient() {
        return WebClient.builder()
                .baseUrl(evolutionUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("apikey", evolutionKey)
                .build();
    }
}
