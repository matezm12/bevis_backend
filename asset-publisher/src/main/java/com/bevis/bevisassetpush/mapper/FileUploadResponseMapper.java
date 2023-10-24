package com.bevis.bevisassetpush.mapper;

import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.filecore.domain.File;
import com.bevis.master.domain.Master;
import com.bevis.asset.FieldsUtil;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.gateway.UrlGatewayService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileUploadResponseMapper {

    private final UrlGatewayService urlGatewayService;
    private final BlockchainUrlGateway blockchainUrlGateway;

    @NotNull
    public Map<String, String> mapAsset(Master master, AssetDTO asset, File file) {
        Map<String, String> result = new HashMap<>();
        result.put("assetId", asset.getAssetId());
        result.putAll(FieldsUtil.convertMapValuesToString(asset.getFields()));
        result.put("dateTimeCreated", master.getGenDate().toString());
        result.put("blockchain", master.getBlockchain().getName());
        result.put("publicKey", master.getPublicKey());
        result.put("transactionId", file.getTransactionId());
        result.put("ipfsCid", file.getIpfs());
        result.put("transactionIdLink", blockchainUrlGateway.getBlockchainTransactionLink(file.getTransactionId(), master.getBlockchain()));
        result.put("ipfsCidLink", urlGatewayService.getIpfsUrl(file.getIpfs()));
        result.put("sha256Hash", file.getSha256Hash());
        result.put("costInUnits", result.get("priceInUnits") + " U");
        return result;
    }
}
