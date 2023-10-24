package com.bevis.webapp.masterdetails.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class MasterDetailsDTO {
    private String assetId;
    private String publicKey;
    private String publicKeyUrl;
    private Instant genDate;
    private Instant importDate;
    private String codereadrScanId;
    private Boolean activationStatus;
    private Boolean isCsc;
    private Boolean isLocked;

    private MasterImportDTO masterImport;
    private BlockchainDTO blockchain;
    private AssetTypeDTO assetType;
    private FileDTO file;
    private String creatorAssetId;
    private String ownerAssetId;
}
