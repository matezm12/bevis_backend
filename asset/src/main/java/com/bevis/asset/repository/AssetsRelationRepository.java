package com.bevis.asset.repository;

import com.bevis.asset.domain.AssetsRelation;
import com.bevis.master.domain.Master;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AssetsRelationRepository extends JpaRepository<AssetsRelation, Long> {

    Page<AssetsRelation> findByAssetId(String assetId, Pageable pageable);

    List<AssetsRelation> findByAssetId(String assetId);

    Page<AssetsRelation> findByParentAssetId(String parentAssetId, Pageable pageable);

    List<AssetsRelation> findByParentAssetId(String parentAssetId);

    boolean existsByAssetAndParentAsset(Master asset, Master parentAsset);

    boolean existsByAssetIdAndParentAssetId(String assetId, String parentAssetId);

    void deleteByAssetId(String assetId);

    void deleteByAssetIdAndParentAssetIdIn(String assetId, Collection<String> ids);

    long countByParentAssetId(String parentAssetId);
}
