package com.bevis.appbe.web.rest.webapp;

import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.balance.dto.CryptoToken;
import com.bevis.blockchainfile.dto.AssetFilesDTO;
import com.bevis.coininfo.dto.CoinDetails;
import com.bevis.nft.nfttoken.dto.NftDto;
import com.bevis.webapp.AssetDetailsService;
import com.bevis.webapp.dto.AssetIdDto;
import com.bevis.webapp.masterdetails.dto.MasterDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/webapp")
public class AssetDetailsController {

    private final AssetDetailsService assetDetailsService;

    @PostMapping("assets")
    List<MasterDetailsDTO> getAssetsByIDs(@RequestBody List<AssetIdDto> assetIds) {
        log.debug("REST request to get Master by publicKeys: {}", assetIds);
        return assetDetailsService.getAssetsByIDs(assetIds);
    }

    @GetMapping("assets/{assetId}")
    MasterDetailsDTO findOne(@PathVariable String assetId) {
        log.debug("REST request to get Master: {}", assetId);
        return assetDetailsService.getMasterDetails(assetId);
    }

    @GetMapping("assets/{assetId}/groups")
    AssetGroupsInfoWrapper getFullAssetInfo(@PathVariable String assetId) {
        log.debug("REST request to get Asset by id : {}", assetId);
        return assetDetailsService.getFullAssetInfo(assetId);
    }

    @GetMapping("assets/{assetId}/files")
    AssetFilesDTO getAssetFiles(@PathVariable String assetId) {
        log.debug("REST request to get Files by assetId : {} ", assetId);
        return assetDetailsService.getAssetFiles(assetId);
    }

    @GetMapping("assets/{assetId}/parents")
    DataResponse<MasterDetailsDTO> findMasterParents(@PathVariable String assetId, Pageable pageable) {
        log.debug("REST request to get children if Master ID:{}", assetId);
        return DataResponse.of(assetDetailsService.findMasterParents(assetId, pageable));
    }

    @GetMapping("assets/{assetId}/children")
    DataResponse<MasterDetailsDTO> findMasterChildren(@PathVariable String assetId, Pageable pageable) {
        log.debug("REST request to get children if Master ID:{}", assetId);
        return DataResponse.of(assetDetailsService.findMasterChildren(assetId, pageable));
    }

    @GetMapping("assets/{assetId}/tokens")
    DataResponse<CryptoToken> getCoinTokens(@PathVariable("assetId") String assetId) {
        log.debug("REST request to get Coin tokens by assetID : {}", assetId);
        return DataResponse.of(assetDetailsService.getCoinTokens(assetId));
    }

    @GetMapping("assets/{assetId}/nfts")
    DataResponse<NftDto> getCoinNftTokens(@PathVariable("assetId") String assetId) {
        log.debug("REST request to get Coin tokens by assetId : {}", assetId);
        return DataResponse.of(assetDetailsService.getCoinNftTokens(assetId));
    }

    @GetMapping("assets/{assetId}/coin-details")
    CoinDetails getCoinDetails(@PathVariable("assetId") String coinId,
                               @RequestParam(value = "fiat-currency", defaultValue = "USD") String fiatCurrency) {
        log.debug("REST request to get Coin details by coinId : {} ", coinId);
        return assetDetailsService.getCoinDetails(coinId, fiatCurrency);
    }

}
