package com.dariodussin.whatsappautomationbackend.service;

import com.dariodussin.whatsappautomationbackend.model.JobStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.dariodussin.whatsappautomationbackend.model.ScheduleJob;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupabaseApiService {

    private final WebClient supabaseClient;

    public SupabaseApiService(@Qualifier("supabaseClient") WebClient supabaseClient) {
        this.supabaseClient = supabaseClient;
    }

    @SuppressWarnings("unchecked")
    public List<ScheduleJob> fetchPendingTasks() {
        // Current time in UTC ISO 8601 format
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        String nowString = nowUtc.format(DateTimeFormatter.ISO_INSTANT);

        // Start of the current year in UTC
        String startOfYear = ZonedDateTime.of(nowUtc.getYear(), 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        return supabaseClient.get()
                .uri(uri -> uri.path("schedule_jobs")
                        .queryParam("status", "eq.pending")
                        .queryParam("scheduled_at", "lte." + nowString) // Must be due now
                        .queryParam("scheduled_at", "gte." + startOfYear) // Must be from this year
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ScheduleJob>>() {
                })
                .block();
    }

    public void updateJobStatus(String jobId, JobStatus status, String errorMessage) {
        try {
            // Log the status change locally for Kubernetes logs
            System.out.printf("[DB-UPDATE] Job: %s | New Status: %s%n", jobId, status);

            Map<String, Object> body = new HashMap<>();
            body.put("status", status.name().toLowerCase());

            if (errorMessage != null) {
                body.put("error_message", errorMessage); // Useful for debugging from the UI
            }

            supabaseClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path("schedule_jobs")
                            .queryParam("id", "eq." + jobId) // Filter: WHERE id = jobId
                            .build())
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(s -> s.isError(), response ->
                            response.bodyToMono(String.class).flatMap(error -> {
                                // Intelligent Error: Captures RLS or Database constraint issues
                                System.err.println("[SUPABASE-ERROR] Patch Failed: " + error);
                                return Mono.error(new RuntimeException("Supabase Patch Error"));
                            })
                    )
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("[CRITICAL] Failed to update Supabase status: " + e.getMessage());
        }
    }

    public String getInstanceNameByCampaignId(String campaignId) {
        Map<String, Object> response = supabaseClient.get()
                .uri(uri -> uri.path("whatsapp_instances")
                        .queryParam("campaign_id", "eq." + campaignId)
                        .queryParam("select", "instance_name")
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .next() // Get the first match
                .block();

        return response != null ? (String) response.get("instance_name") : null;
    }

    public List<String> getGroupIdsByCampaignId(String campaignId) {
        return supabaseClient.get()
                .uri(uri -> uri.path("campaign_groups")
                        .queryParam("campaign_id", "eq." + campaignId)
                        .queryParam("select", "group_id")
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                // Extract the "group_id" string from each map in the list
                .map(row -> (String) row.get("group_id"))
                .collectList()
                .block(); // Returns a List of all Group JIDs (@g.us)
    }
}