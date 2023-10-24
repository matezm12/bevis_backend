package com.bevis.assetinfo.util;

import com.bevis.assetinfo.dto.MasterData;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.master.domain.Master;
import lombok.NonNull;

import java.util.Optional;

public final class MasterFetchUtil {
    public static MasterData mapMasterData(Master master) {
        return MasterData.builder()
                .publicKey(master.getPublicKey())
                .assetId(master.getId())
                .genDate(master.getGenDate().toString())
                .build();
    }

    public static String getMasterCurrency(@NonNull Master master) {
        return Optional.of(master)
                .map(Master::getBlockchain)
                .map(Blockchain::getName)
                .map(String::toUpperCase)
                .orElse(null);
    }

    public static String getMasterCurrencyCode(Master master) {
        return Optional.ofNullable(master.getBlockchain())
                .map(Blockchain::getCurrencyCode)
                .map(String::toUpperCase)
                .orElse(null);
    }

    public static  boolean hasCoinTokens(Master master) {
        return Optional.of(master)
                .map(Master::getBlockchain)
                .map(Blockchain::getHasTokens)
                .orElse(false);
    }

}
