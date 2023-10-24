package com.bevis.appbe.web.rest.exception;

import com.bevis.appbe.web.rest.vm.FieldErrorVM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Instant timestamp;
    private String error;
    private String path;
    private String type;
    private Integer status;
    private String message;
    private String detailedMessage;
    private List<FieldErrorVM> fieldErrors;
}
