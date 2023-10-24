package com.bevis.assetinfo.mapper;

import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.assetinfo.dto.FileAssetGroup;
import com.bevis.assetinfo.dto.AssetGroup;
import com.bevis.assetinfo.dto.AssetGroupsInfo;
import org.mapstruct.Mapper;

@Mapper
public interface AssetInfoGroupMapper {
    AssetGroupsInfoWrapper map(AssetGroupsInfo assetGroupsInfo);
    FileAssetGroup map(AssetGroup assetGroup);
}
