package com.bevis.assetimport.dto;

import lombok.Data;

@Data
public class AssetImportFilter {
    private String search;
    private Boolean onlyUnprocessed;
    private String serviceId;
}
