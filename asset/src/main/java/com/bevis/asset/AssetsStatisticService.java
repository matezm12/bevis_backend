package com.bevis.asset;

import com.bevis.asset.dto.AssetStatisticDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetsStatisticService {
    Page<AssetStatisticDTO> loadStatistic(String search, Pageable pageable);
}
