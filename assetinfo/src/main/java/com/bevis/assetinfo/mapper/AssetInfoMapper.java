package com.bevis.assetinfo.mapper;

import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.master.domain.Master;
import com.bevis.gateway.UrlGatewayService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AssetInfoMapper {

    public final static Set<String> BLACKLISTED_FIELDS = new HashSet<>(Arrays.asList(
            "asset_id",
            "product_type_asset_id",
            "import_id",
            "device_id",
            "codereadr"
    ));

    private final UrlGatewayService urlGatewayService;
    private final BlockchainUrlGateway blockchainUrlGateway;

    public String getIpfsUrl(String ipfs) {
        return urlGatewayService.getIpfsUrl(ipfs);
    }

    public String getBlockchainAddressLink(@NonNull Master master) {
        Blockchain blockchain = master.getBlockchain();
        if (Objects.isNull(blockchain)) {
            return null;
        }
        return blockchainUrlGateway.getBlockchainAddressLink(master.getPublicKey(), blockchain);
    }
}
