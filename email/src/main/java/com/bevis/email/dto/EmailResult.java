package com.bevis.email.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EmailResult {
    Map<String, Object> params;
}
