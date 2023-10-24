package com.bevis.assetimport.repository;

import com.bevis.assetimport.domain.AssetImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AssetImportRepository extends JpaRepository<AssetImport, Long>, JpaSpecificationExecutor<AssetImport> {
    Optional<AssetImport> findByScanId(String scanId);
}
