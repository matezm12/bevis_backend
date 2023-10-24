package com.bevis.assetimport.dto;

import com.bevis.assettype.domain.AssetType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private String assetId;
    private AssetType assetType;
}
