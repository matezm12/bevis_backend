package com.bevis.assetinfo.impl;

import com.bevis.assetinfo.AssetGroupsInfoService;
import com.bevis.assetinfo.DynamicRawAssetInfoLoadingService;
import com.bevis.assetinfo.LogoFetchingService;
import com.bevis.assetinfo.dto.*;
import com.bevis.assetinfo.mapper.AssetInfoMapper;
import com.bevis.coininfo.CoinInfoBalanceService;
import com.bevis.coininfo.dto.CoinInfo;
import com.bevis.assettype.domain.AssetType;
import com.bevis.master.domain.Master;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bevis.assetinfo.mapper.AssetInfoMapper.BLACKLISTED_FIELDS;
import static com.bevis.assetinfo.util.MasterFetchUtil.getMasterCurrency;
import static com.bevis.assetinfo.util.MasterFetchUtil.mapMasterData;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DynamicRawAssetInfoLoadingServiceImpl implements DynamicRawAssetInfoLoadingService {

    private final static String ASSET_ID = "asset_id";

    private final CoinInfoBalanceService coinInfoBalanceService;
    private final AssetGroupsInfoService assetGroupsInfoService;
    private final LogoFetchingService logoFetchingService;
    private final AssetInfoMapper assetInfoMapper;

    @Override
    public AssetInfo getAssetInfo(Master master, String currency) {
        return AssetInfo.builder()
                .assetType(master.getAssetType())
                .displayValues(loadDisplayValues(master))
                .coinInfo(getCoinInfo(master, currency))
                .master(mapMasterData(master))
                .product(mapProductData(master))
                .location(null)
                .shipment(null)
                .build();
    }

    private ProductData mapProductData(Master master) {
        return ProductData.builder()
                .img(logoFetchingService.loadFileUrlByMaster(master))
                .build();
    }

    //FIXME FIX THIS
    private List<AssetValue> loadDisplayValues(Master master) {
        return Stream.concat(
                Stream.of(
                        AssetValue.builder()
                                .fieldName("blockchain")
                                .fieldTitle("Blockchain")
                                .fieldValue(getMasterCurrency(master))
                                .build(),
                        AssetValue.builder()
                                .fieldName("public_key")
                                .fieldTitle("Public Key")
                                .fieldValue(master.getPublicKey())
                                .link(assetInfoMapper.getBlockchainAddressLink(master))
                                .build()
                ),
                loadDisplayValuesFromGroups(master).stream()
        ).collect(Collectors.toList());
    }

    private List<AssetValue> loadDisplayValuesFromGroups(Master master) {
        return assetGroupsInfoService.loadAssetGroups(master).stream()
                .flatMap(assetGroup -> Stream.concat(
                        Stream.of(
                                assetValueGroup(assetGroup),
                                assetValueGroupAssetId(assetGroup)
                        ),
                        assetGroup.getAssetValues().stream()
                ))
                .filter(x -> !BLACKLISTED_FIELDS.contains(x.getFieldName()))
                .collect(Collectors.toList());
    }

    private AssetValue assetValueGroup(AssetGroup assetGroup) {
        return AssetValue.builder()
                .fieldName(assetGroup.getGroupName())
                .fieldTitle(assetGroup.getGroupName() + " data")
                .fieldValue("")
                .build();
    }

    private AssetValue assetValueGroupAssetId(AssetGroup assetGroup) {
        String groupAssetId = Optional.of(assetGroup)
                .map(AssetGroup::getAssetValues)
                .orElse(Collections.emptyList())
                .stream()
                .filter(x -> Objects.equals(x.getFieldName(), ASSET_ID))
                .findFirst()
                .map(AssetValue::getFieldValue)
                .orElse(null);

        return AssetValue.builder()
                .fieldName(assetGroup.getGroupName())
                .fieldTitle(assetGroup.getGroupName() + " ID")
                .fieldValue(groupAssetId)
                .build();
    }

    private CoinInfo getCoinInfo(Master master, String currency) {
        AssetType assetType = master.getAssetType();
        String publicKey = master.getPublicKey();
        String sourceCurrency = getMasterCurrency(master);
        boolean isTopup = assetType != null && Boolean.TRUE.equals(assetType.getIsCsc());
        if (isTopup & Objects.nonNull(sourceCurrency)) {
            try {
                return coinInfoBalanceService.getCoinInfo(publicKey, sourceCurrency, currency);
            }catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }
        return null;
    }

}
