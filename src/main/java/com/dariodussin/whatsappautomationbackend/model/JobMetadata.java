package com.dariodussin.whatsappautomationbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JobMetadata(
        @JsonProperty("group_id") String groupId,
        @JsonProperty("group_name") String groupName,
        @JsonProperty("schedule_date") String scheduleDate,
        @JsonProperty("schedule_time") String scheduleTime,
        String message,      // Matches 'text' in Evolution API
        @JsonProperty("file_url") String fileUrl,
        @JsonProperty("mention_all") boolean mentionAll
) {}