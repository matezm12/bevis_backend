package com.bevis.appbe.web.rest.bevisapp;

import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.appbe.web.rest.vm.PublicKeysVm;
import com.bevis.assetinfo.AssetInfoService;
import com.bevis.assetinfo.dto.AssetInfo;
import com.bevis.blockchainfile.BlockchainAssetInfoService;
import com.bevis.blockchainfile.dto.AssetFilesDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class BevisAssetInfoController {

    private final AssetInfoService assetInfoService;
    private final BlockchainAssetInfoService blockchainAssetInfoService;

    /**
     * Loafing information about asset by it's public key
     *
     * @param publicKey - identifier of asset
     * @param currency  - not needed for most of assets
     * @return
     */
    @GetMapping("/v2/asset-info")
    AssetInfo getAssetInfo(@RequestParam("public_key") String publicKey, String currency) {
        log.debug("REST request to get Asset by public key : {} and currency : {}", publicKey, currency);
        return assetInfoService.getAssetInfoByPublicKeyAndCurrency(publicKey, currency);
    }

    /**
     * Loafing information about assets by theirs public keys
     *
     * @param body     - list of public keys
     * @param currency - not needed for most of assets
     * @return
     */
    @PostMapping("/v2/asset-info")
    DataResponse<AssetInfo> getAssetsInfo(@RequestBody @Valid PublicKeysVm body, String currency) {
        log.debug("REST request to get Asset by public keys : {} and currency : {}", body, currency);
        return DataResponse.of(assetInfoService.getAssetsInfoByPublicKeysAndCurrency(body.getPublicKeys(), currency));
    }

    /**
     * Loafing files for asset by it's public key
     *
     * @param assetId - identifier of asset
     * @return
     */
    @GetMapping("/v2/asset-files")
    AssetFilesDTO getAssetFiles(@RequestParam("asset-id") String assetId) {
        log.debug("REST request to get Files by assetId : {} ", assetId);
        return blockchainAssetInfoService.getFilesByAssetIdOrPublicKey(assetId);
    }
}
