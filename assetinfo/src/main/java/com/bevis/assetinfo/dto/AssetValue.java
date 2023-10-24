package com.bevis.assetinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AssetValue {
    private String fieldName;
    private String fieldTitle;
    private String fieldValue;
    private String link;
    private boolean isAsset;
}
