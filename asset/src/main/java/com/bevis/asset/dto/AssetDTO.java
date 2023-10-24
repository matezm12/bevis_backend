package com.bevis.asset.dto;

import com.bevis.master.domain.Master;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class AssetDTO {
    private String assetId;
    private Map<String, FieldValueDTO> fields;
    private Master master;
    private Map<String, Object> fieldsSchema;
}
