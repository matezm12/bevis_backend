package com.bevis.assetinfo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MasterData {
    private String assetId;
    private String publicKey;
    private String genDate;

}
