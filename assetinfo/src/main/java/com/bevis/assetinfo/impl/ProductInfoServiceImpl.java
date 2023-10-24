package com.bevis.assetinfo.impl;

import com.bevis.assetinfo.LogoFetchingService;
import com.bevis.assetinfo.ProductInfoService;
import com.bevis.assetinfo.dto.ProductInfoDTO;
import com.bevis.assettype.domain.AssetType;
import com.bevis.asset.DynamicAssetService;
import com.bevis.asset.dto.AssetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductInfoServiceImpl implements ProductInfoService {

    private final DynamicAssetService dynamicAssetService;
    private final LogoFetchingService logoFetchingService;

    @Override
    public ProductInfoDTO getById(String assetId) {
        AssetDTO asset = dynamicAssetService.getById(assetId);
        AssetType assetType = asset.getMaster().getAssetType();
        assert assetType.getIsProduct();
        String logo = logoFetchingService.loadFileUrlByMaster(asset.getMaster());
        return ProductInfoDTO.builder()
                .assetId(assetId)
                .fields(asset.getFields())
                .logo(logo)
                .assetTypeId(assetType.getId())
                .build();
    }
}
