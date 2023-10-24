package com.bevis.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class AssetRequest {

    @NotNull
    @NotEmpty
    private String assetId;

    @NotNull
    private Map<String, FieldValueDTO> fieldValues = new HashMap<>();

    private Set<String> fieldsToRemove = new HashSet<>();

    private Long assetTypeId;
}
