package com.argo.common.domain.channel;

public enum ChannelType {
    ONLINE("온라인"),
    OFFLINE("오프라인");

    private String description;

    ChannelType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
