package com.bevis.appbe.web.rest.vm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponseVM {
    private String message;
}
