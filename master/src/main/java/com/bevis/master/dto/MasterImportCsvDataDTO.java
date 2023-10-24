package com.bevis.master.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MasterImportCsvDataDTO {
    private List<AssetKeyCsvDTO> assetKeys;
    private String fileName;
}
