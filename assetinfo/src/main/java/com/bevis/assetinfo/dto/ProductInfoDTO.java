package com.bevis.assetinfo.dto;

import com.bevis.asset.dto.FieldValueDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class ProductInfoDTO {
    private String assetId;
    private String logo;
    private Long assetTypeId;
    private Map<String, FieldValueDTO> fields;
}
