package com.bevis.assetimport;

import com.bevis.assetimport.domain.AssetImport;

import java.util.List;

public interface AssetUnImportService {
    AssetImport undoAssetImport(AssetImport assetImport) throws AssetImportException;

    void undoMastersByScanIdAndPublicKeyNotIn(String scanId, List<String> newMasters) throws AssetImportException;
}
