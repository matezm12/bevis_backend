package com.bevis.assetimport;

import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.domain.enumeration.CodeReadrServiceType;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.ImportWrappingDTO;
import com.bevis.assettype.domain.AssetType;
import com.bevis.asset.dto.AssetRequest;

public interface BaseImportService {
    AssetImport importAssets(AssetImportDTO assetImportDTO);

    AssetType loadAssetType(AssetImport assetImport, ImportWrappingDTO assetImportDTO) throws AssetImportException;

    AssetRequest getProductRequestFromImport(String assetId, ImportWrappingDTO assetImportDTO);

    CodeReadrServiceType getServiceKey();
}
