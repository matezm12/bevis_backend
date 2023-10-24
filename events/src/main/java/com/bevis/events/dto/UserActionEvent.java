package com.bevis.events.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserActionEvent {
    private String action;
    private ActionStatus actionStatus;
    private String user;
    private String details;
    private String link;

    public enum ActionStatus {
        SUCCESS, FAILURE
    }
}
