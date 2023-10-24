package com.bevis.lister.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListerAssetRequest {

    @NotNull
    private String assetId;

    private String name;
}
