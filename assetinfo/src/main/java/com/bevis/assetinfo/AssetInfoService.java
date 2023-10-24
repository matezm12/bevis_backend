package com.bevis.assetinfo;

import com.bevis.assetinfo.dto.AssetInfo;

import java.util.List;

public interface AssetInfoService {

    AssetInfo getAssetInfoByPublicKeyAndCurrency(String publicKey, String currency);

    List<AssetInfo> getAssetsInfoByPublicKeysAndCurrency(List<String> publicKeys, String currency);
}
