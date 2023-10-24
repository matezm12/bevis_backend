package com.bevis.lister.impl;

import com.bevis.assettype.domain.AssetType;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.coininfo.CoinInfoBalanceService;
import com.bevis.filecore.domain.File;
import com.bevis.gateway.UrlGatewayService;
import com.bevis.lister.ListerAsset;
import com.bevis.lister.ListerAssetInfoService;
import com.bevis.lister.dto.ListerAssetResponse;
import com.bevis.common.util.DateUtil;
import com.bevis.master.domain.Master;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.bevis.common.util.StringUtil.isUrl;
import static com.bevis.common.util.StringUtil.toUpperCase;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
class ListerAssetInfoServiceImpl implements ListerAssetInfoService {

    private final String DEFAULT_DISPLAY_CURRENCY = "USD";

    private final UrlGatewayService urlGatewayService;
    private final CoinInfoBalanceService coinInfoBalanceService;

    @Transactional(readOnly = true)
    @Override
    public Page<ListerAssetResponse> toDto(Page<ListerAsset> listerAssets, String displayCurrency){
        return listerAssets.map(x -> toDTO(x, displayCurrency != null ? displayCurrency : "USD"));
    }

    @Transactional(readOnly = true)
    @Override
    public ListerAssetResponse toDTO(ListerAsset listerAsset) {
        return toDTO(listerAsset, DEFAULT_DISPLAY_CURRENCY);
    }

    private ListerAssetResponse toDTO(ListerAsset listerAsset, String displayCurrency){
        Master master = listerAsset.getMaster();
        AssetType assetType = master.getAssetType();

        ListerAssetResponse listerAssetResponse = new ListerAssetResponse();

        listerAssetResponse.setName(listerAsset.getName());
        listerAssetResponse.setAssetId(master.getId());
        listerAssetResponse.setCreatedDate(DateUtil.convertInstantToLocalDate(master.getGenDate()));
        listerAssetResponse.setAssetTypeName(Objects.nonNull(assetType) ? assetType.getName() : null);

        File file = master.getFile();
        if (Objects.nonNull(file)) {
            final String cid = file.getIpfs();
            if (Objects.nonNull(cid) && !isUrl(cid)) {
                listerAssetResponse.setFileUrl(urlGatewayService.getIpfsUrl(cid));
                listerAssetResponse.setFileType(file.getFileType());
                listerAssetResponse.setEncrypted(file.getEncrypted());
            }
        }
        if (Objects.nonNull(assetType) && Objects.equals(Boolean.TRUE, assetType.getIsCsc())) {
            String coinCurrency = getCoinCurrency(master);
            listerAssetResponse.setCoinInfo(coinInfoBalanceService.getCoinInfo(master.getPublicKey(), coinCurrency, displayCurrency));
        }
        return listerAssetResponse;
    }

    private String getCoinCurrency(Master master) {
        Blockchain blockchain = master.getBlockchain();
        if (Objects.nonNull(blockchain) && Objects.nonNull(blockchain.getName())) {
            return toUpperCase(blockchain.getName());
        }
        return "";
    }
}
