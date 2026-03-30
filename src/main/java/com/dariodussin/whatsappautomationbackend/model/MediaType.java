package com.dariodussin.whatsappautomationbackend.model;

import lombok.Getter;

@Getter
public enum MediaType {
    IMAGE("image", "image/png"),
    AUDIO("audio", "audio/mpeg"),
    VIDEO("video", "video/mp4"),
    DOCUMENT("document", "application/pdf");

    private final String evolutionValue; // What Evolution API expects
    private final String mimeType;       // Default mimetype for this category

    MediaType(String evolutionValue, String mimeType) {
        this.evolutionValue = evolutionValue;
        this.mimeType = mimeType;
    }

    /**
     * Helper to convert "send_image" string to this Enum
     */
    public static MediaType fromJobType(JobType jobType) {
        String type = jobType.getValue().replace("send_", "").toUpperCase();
        try {
            return MediaType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return DOCUMENT; // Default fallback
        }
    }
}