package com.bevis.assetinfo;

import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.master.domain.Master;

public interface AdminAssetGroupsInfoService {
    AssetGroupsInfoWrapper getAssetInfo(String assetId);
    AssetGroupsInfoWrapper getAssetInfo(Master master);
}
