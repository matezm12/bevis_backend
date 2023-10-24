package com.bevis.events.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorEvent {
    private String serviceName;
    private String message;
}
