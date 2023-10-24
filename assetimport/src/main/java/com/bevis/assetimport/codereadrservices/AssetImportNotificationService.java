package com.bevis.assetimport.codereadrservices;

import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.ImportWrappingDTO;

public interface AssetImportNotificationService {
    void notifyImportCreated(ImportWrappingDTO importWrappingDTO);
    void notifyImportFailed(AssetImportDTO dynamicAssetImport, String error);
}
