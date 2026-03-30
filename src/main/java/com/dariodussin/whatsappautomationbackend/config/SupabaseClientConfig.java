package com.dariodussin.whatsappautomationbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SupabaseClientConfig {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.role}")
    private String serviceRoleKey;

    @Bean
    public WebClient supabaseClient() {
        return WebClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader("Content-Type", "application/json")
                // Use the serviceRoleKey for BOTH headers to ensure RLS bypass
                .defaultHeader("apikey", serviceRoleKey)
                .defaultHeader("Authorization", "Bearer " + serviceRoleKey)
                .build();
    }
}