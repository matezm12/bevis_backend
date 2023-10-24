package com.bevis.assetinfo.impl;

import com.bevis.assetinfo.AssetInfoService;
import com.bevis.assetinfo.DynamicRawAssetInfoLoadingService;
import com.bevis.assetinfo.dto.AssetInfo;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.master.domain.Master;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
class AssetInfoServiceImpl implements AssetInfoService {

    private final MasterService masterService;
    private final DynamicRawAssetInfoLoadingService dynamicRawAssetInfoLoadingService;

    @Transactional(readOnly = true)
    @Override
    public AssetInfo getAssetInfoByPublicKeyAndCurrency(String publicKey, String currency) {
        Master master = masterService.findFirstByPublicKeyOrId(publicKey, publicKey)
                .orElseThrow(() -> new ObjectNotFoundException("Master with public key " + publicKey + " not found "));
        return getAssetInfo(master, currency);
    }

    @Override
    public List<AssetInfo> getAssetsInfoByPublicKeysAndCurrency(List<String> publicKeys, String currency) {
        return publicKeys.stream().map(publicKey -> {
            Master master = masterService.findFirstByPublicKeyOrId(publicKey, publicKey)
                    .orElseThrow(() -> new ObjectNotFoundException("Master with public key " + publicKey + " not found "));
            return getAssetInfo(master, currency);
        }).collect(Collectors.toList());
    }

    private AssetInfo getAssetInfo(Master master, String currency) {
        try {
            return dynamicRawAssetInfoLoadingService.getAssetInfo(master, currency);
        } catch (Exception e) {
            log.error(e.getMessage());
            return AssetInfo.builder().build();
        }
    }
}
