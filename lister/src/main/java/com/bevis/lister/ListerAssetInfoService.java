package com.bevis.lister;

import com.bevis.lister.dto.ListerAssetResponse;
import org.springframework.data.domain.Page;

public interface ListerAssetInfoService {
    Page<ListerAssetResponse> toDto(Page<ListerAsset> listerAssets, String displayCurrency);

    ListerAssetResponse toDTO(ListerAsset listerAsset);
}
