package com.bevis.certificatecore.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CertPublishDTO {
    @NotNull
    private String assetId;
}

