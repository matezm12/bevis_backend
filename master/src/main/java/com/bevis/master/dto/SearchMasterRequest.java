package com.bevis.master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMasterRequest {

    public static final String SKU = "sku";

    private String search;

    private String codereadrScanId;
    private Long assetTypeId;
    private Long masterImportId;
    private Long blockchainId;
    private Boolean productsOnly;
    private Boolean cscOnly;
    private Boolean showInactive;

    private Map<String, String> dynamicFilter;
}
