package com.bevis.master.dto;

import com.bevis.assettype.domain.AssetType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class MasterPartUpdate {
    @NotNull
    private String id;
    private String publicKey;
    private Instant genDate;
    private AssetType assetType;
    private Boolean isCsc;
    private Boolean isLocked;
    private Boolean isActive;
    private String ownerAssetId;
}
