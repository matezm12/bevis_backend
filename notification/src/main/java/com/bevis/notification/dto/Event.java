package com.bevis.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private String subject;
    private String message;
}
