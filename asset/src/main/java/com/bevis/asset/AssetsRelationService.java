package com.bevis.asset;

import com.bevis.master.domain.Master;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface AssetsRelationService {

    boolean exists(String assetId, String parentAssetId);

    boolean exists(Master asset, Master parentAsset);

    List<Master> findChildrenOfAsset(Master master);

    List<Master> findChildrenOfAsset(Master master, boolean recursively);

    List<Master> findChildrenOfAssetId(String assetId);

    Page<Master> findChildrenOfAssetId(String assetId, Pageable pageable);

    List<Master> findChildrenOfAssetId(String assetId, boolean recursively);

    List<Master> findParentsOfAsset(Master master);

    List<Master> findParentsOfAsset(Master master, boolean recursively);

    List<Master> findParentsOfAssetId(String assetId);

    Page<Master> findParentsOfAssetId(String assetId, Pageable pageable);

    List<Master> findParentsOfAssetId(String assetId, boolean recursively);

    void saveRelation(Master asset, Master parentAsset);

    void saveRelations(String assetId, Collection<String> parentAssetIds);

    void deleteRelationsByAssetId(String assetId, Collection<String> parentAssetIds);

    void deleteByAssetId(String assetId);

    Long getChildrenCount(String assetId);
}
