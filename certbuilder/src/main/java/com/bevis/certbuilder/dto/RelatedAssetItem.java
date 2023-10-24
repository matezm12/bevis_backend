package com.bevis.certbuilder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelatedAssetItem {
    private String assetId;
    private String assetUrl;
    private String publicKey;
    private String publicKeyUrl;
    private String genDate;
    private String vendor;
    private String assetType;
}
