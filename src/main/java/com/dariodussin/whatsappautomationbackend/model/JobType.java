package com.dariodussin.whatsappautomationbackend.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum JobType {
    SEND_MESSAGE("send_message"),
    SEND_IMAGE("send_image"),
    SEND_AUDIO("send_audio"),
    SEND_VIDEO("send_video"),
    SEND_DOCUMENT("send_document"),
    RENAME_GROUP("rename_group"),
    OPEN_GROUP("open_group"),
    CLOSE_GROUP("close_group");

    private final String value;

    JobType(String value) {
        this.value = value;
    }

    @JsonValue // Tells Jackson to use "send_message" instead of "SEND_MESSAGE"
    public String getValue() {
        return value;
    }
}