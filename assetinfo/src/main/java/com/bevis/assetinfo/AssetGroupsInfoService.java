package com.bevis.assetinfo;

import com.bevis.assetinfo.dto.AssetGroup;
import com.bevis.assetinfo.dto.AssetGroupsInfo;
import com.bevis.master.domain.Master;

import java.util.List;

public interface AssetGroupsInfoService {
    AssetGroupsInfo getAssetInfo(String assetId);

    AssetGroupsInfo getAssetInfo(Master master);

    List<AssetGroup> loadAssetGroups(Master master);
}
