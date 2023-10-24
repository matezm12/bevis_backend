package com.bevis.webapp;

import com.bevis.assetinfo.AdminAssetGroupsInfoService;
import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.balance.dto.CryptoToken;
import com.bevis.blockchainfile.BlockchainAssetInfoService;
import com.bevis.blockchainfile.dto.AssetFilesDTO;
import com.bevis.coininfo.CoinDetailsService;
import com.bevis.coininfo.CoinTokensService;
import com.bevis.coininfo.dto.CoinDetails;
import com.bevis.coininfo.dto.CoinRequest;
import com.bevis.master.domain.Master;
import com.bevis.asset.AssetsRelationService;
import com.bevis.master.MasterService;
import com.bevis.nft.nfttoken.dto.NftDto;
import com.bevis.webapp.dto.AssetIdDto;
import com.bevis.webapp.masterdetails.MasterDetailsService;
import com.bevis.webapp.masterdetails.dto.MasterDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AssetDetailsServiceImpl implements AssetDetailsService {

    private final MasterDetailsService masterDetailsService;

    private final AdminAssetGroupsInfoService adminAssetGroupsInfoService;
    private final BlockchainAssetInfoService blockchainAssetInfoService;
    private final MasterService masterService;
    private final AssetsRelationService assetsRelationService;
    private final CoinTokensService coinTokensService;
    private final CoinDetailsService coinDetailsService;

    @Override
    public List<MasterDetailsDTO> getAssetsByIDs(List<AssetIdDto> assetIds) {
        return masterDetailsService.getAssetsByIDs(assetIds);
    }

    @Override
    public MasterDetailsDTO getMasterDetails(String assetId) {
        return masterDetailsService.getMasterDetails(assetId);
    }

    @Override
    public AssetGroupsInfoWrapper getFullAssetInfo(String assetId) {
        return adminAssetGroupsInfoService.getAssetInfo(assetId);
    }

    @Override
    public AssetFilesDTO getAssetFiles(String assetId) {
        return blockchainAssetInfoService.getFilesByAssetIdOrPublicKey(assetId);
    }

    @Override
    public Page<MasterDetailsDTO> findMasterParents(String assetId, Pageable pageable) {
        log.debug("REST request to get children if Master ID:{}", assetId);
        return masterService.findByIdOrPublicKey(assetId)
                .map(Master::getId)
                .map(id -> assetsRelationService.findParentsOfAssetId(id, pageable)
                        .map(masterDetailsService::getMasterDetails)
                )
                .orElse(new PageImpl<>(new ArrayList<>()));
    }

    @Override
    public Page<MasterDetailsDTO> findMasterChildren(String assetId, Pageable pageable) {
        return masterService.findByIdOrPublicKey(assetId)
                .map(Master::getId)
                .map(id -> assetsRelationService.findChildrenOfAssetId(id, pageable)
                        .map(masterDetailsService::getMasterDetails)
                )
                .orElse(new PageImpl<>(new ArrayList<>()));
    }

    @Override
    public Page<CryptoToken> getCoinTokens(String assetId) {
        return new PageImpl<>(coinTokensService.getCoinTokens(assetId));
    }

    @Override
    public Page<NftDto> getCoinNftTokens(String assetId) {
        return new PageImpl<>(coinTokensService.getCoinNftTokens(assetId));
    }

    @Override
    public CoinDetails getCoinDetails(String coinId, String fiatCurrency) {
        return coinDetailsService.getCoinDetails(new CoinRequest(coinId, null), fiatCurrency);
    }
}
