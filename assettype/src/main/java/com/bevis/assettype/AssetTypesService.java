package com.bevis.assettype;

import com.bevis.assettype.domain.AssetType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AssetTypesService {

    AssetType getBevisAssetType();

    Optional<AssetType> findById(Long assetTypeId);

    Map<String, Object> getAssetsTypeFieldsByTypeId(Long assetTypeId);

    List<AssetType> getProductAssetTypes();

    Optional<AssetType> findFirstByKey(String key);
}
