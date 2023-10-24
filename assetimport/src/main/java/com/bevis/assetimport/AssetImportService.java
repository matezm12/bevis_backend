package com.bevis.assetimport;

import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.AssetImportFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetImportService {

    AssetImport findOne(Long id);

    Page<AssetImport> findAll(AssetImportFilter filter, Pageable pageable);

    AssetImport importAssets(AssetImportDTO assetImportDTO) throws AssetImportException;

    AssetImport reImportAssets(AssetImport assetImport) throws AssetImportException;

    AssetImport undoAssetImport(Long id) throws AssetImportException;

    void deleteById(Long id) throws AssetImportException;
}
