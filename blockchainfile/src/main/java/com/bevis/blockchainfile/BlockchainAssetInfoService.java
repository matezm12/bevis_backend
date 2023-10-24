package com.bevis.blockchainfile;

import com.bevis.blockchainfile.dto.AssetFilesDTO;

public interface BlockchainAssetInfoService {
    AssetFilesDTO getFilesByAssetIdOrPublicKey(String assetIdOrPublicKey);
}
