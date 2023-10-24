package com.bevis.assetinfo;

import com.bevis.assetinfo.dto.AssetInfo;
import com.bevis.master.domain.Master;

public interface DynamicRawAssetInfoLoadingService {
    AssetInfo getAssetInfo(Master master, String currency);
}
