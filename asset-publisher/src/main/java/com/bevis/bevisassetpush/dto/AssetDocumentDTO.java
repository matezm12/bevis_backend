package com.bevis.bevisassetpush.dto;

import com.bevis.files.dto.File;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AssetDocumentDTO {
    private String assetId;
    private File file;
    private String blockchain;
    private Boolean encrypted;
    private Map<String, Object> dynamicData;
    private FileParametersDTO fileParameters;
}
