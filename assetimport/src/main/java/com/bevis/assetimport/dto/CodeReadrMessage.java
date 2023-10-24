package com.bevis.assetimport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodeReadrMessage {
    private Integer status;
    private String text;
}
