package com.bevis.lister.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AssetNameRequest {

    @NotNull
    private String name;
}
