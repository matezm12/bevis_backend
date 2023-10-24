package com.bevis.assetimport.dto;

import com.bevis.assetimport.domain.enumeration.CodeReadrServiceType;
import com.bevis.filecore.domain.File;
import lombok.Data;

import java.time.Instant;

@Data
public class AssetImportResponse {
    private Long id;
    private String scanId;
    private String serviceId;
    private String upc;
    private String username;
    private Long deviceId;
    private Instant scanDate;
    private Instant uploadDate;
    private String vendorAssetId;
    private String barcode;
    private Long assetsCount;
    private String codereadrBody;
    private String error;
    private Boolean matched;
    private File file;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Instant cancelledAt;
    private Object attributes;

    private Long codereadrServiceId;
    private CodeReadrServiceType serviceName;
}
