package com.bevis.assetimport.dto;

import com.bevis.assetimport.domain.CodeReadrService;
import com.bevis.assettype.domain.AssetType;
import com.bevis.master.domain.Master;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ImportWrappingDTO {

    private final AssetImportDTO dynamicAssetImport;

    private ProductDTO productDTO;
    private AssetType assetType;
    private String operatorAssetId;
    private String vendorAssetId;
    private Master productMaster;
    private List<Master> masters = new ArrayList<>();
    private CodeReadrService codeReadrService;
}
