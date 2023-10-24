package com.bevis.assetinfo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class AssetGroupsInfo {
    private String assetId;
    private String publicKey;
    private String logo;
    private String publicKeyUrl;
    private String blockchain;
    private Instant genDate;
    private String assetType;
    private String codeReadrScanId;
    private Long masterImportId;
    private String masterImportName;
    private List<AssetGroup> assetGroups;
}
