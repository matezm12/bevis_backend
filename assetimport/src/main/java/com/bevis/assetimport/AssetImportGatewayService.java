package com.bevis.assetimport;

import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.domain.AssetImport;

public interface AssetImportGatewayService {
    AssetImport importAssets(AssetImportDTO assetImportDTO) throws AssetImportException;
}
