package com.bevis.assetinfo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AssetValuesWrapper {
    List<AssetValue> assetValues;
    Map<String, String> relatedAssets;
}
