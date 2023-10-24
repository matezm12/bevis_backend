package com.bevis.blockchain.gateway.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
    private Integer statusCode;
}
