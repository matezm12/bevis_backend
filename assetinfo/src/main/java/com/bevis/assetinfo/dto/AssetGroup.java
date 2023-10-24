package com.bevis.assetinfo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssetGroup {
    private String groupKey;
    private String groupName;
    private String groupAssetId;
    private List<AssetValue> assetValues;
}
