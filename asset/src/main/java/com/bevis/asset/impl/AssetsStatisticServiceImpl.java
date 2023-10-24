package com.bevis.asset.impl;

import com.bevis.asset.AssetsRelationService;
import com.bevis.asset.AssetsStatisticService;
import com.bevis.asset.DynamicAssetService;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.asset.dto.AssetStatisticDTO;
import com.bevis.master.dto.SearchMasterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetsStatisticServiceImpl implements AssetsStatisticService {

    private final DynamicAssetService dynamicAssetService;
    private final AssetsRelationService assetsRelationService;

    //TODO Optimize this code. Add master {childrenCount, parentsCount} & update these fields
    @Override
    public Page<AssetStatisticDTO> loadStatistic(String search, Pageable pageable) {
        return dynamicAssetService.findAll(
                        SearchMasterRequest.builder()
                                .productsOnly(true)
                                .search(search)
                                .build(),
                        pageable
                )
                .map(AssetDTO::getMaster)
                .map(master -> AssetStatisticDTO.of(master.getId(), assetsRelationService.getChildrenCount(master.getId())));
    }
}
