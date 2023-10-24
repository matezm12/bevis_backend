package com.bevis.assetinfo.dto;

import com.bevis.blockchainfile.dto.FileDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileAssetGroup {
    private String groupKey;
    private String groupName;
    private String groupAssetId;
    private List<AssetValue> assetValues;
    private List<FileDTO> files;
}
