package com.bevis.coininfo.impl;

import com.bevis.balance.dto.CryptoToken;
import com.bevis.balance.token.CryptoTokenBalanceResolver;
import com.bevis.coininfo.CoinTokensService;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.master.domain.Master;
import com.bevis.master.MasterService;
import com.bevis.nft.nfttoken.dto.NftDto;
import com.bevis.nftcore.nfttoken.NftTokenLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.bevis.assetinfo.util.MasterFetchUtil.getMasterCurrency;
import static com.bevis.assetinfo.util.MasterFetchUtil.hasCoinTokens;

@Service
@Slf4j
@RequiredArgsConstructor
class CoinTokensServiceImpl implements CoinTokensService {

    private final CryptoTokenBalanceResolver cryptoTokenBalanceResolver;
    private final NftTokenLoader nftTokenLoader;
    private final MasterService masterService;

    @Override
    public List<CryptoToken> getCoinTokens(String assetId) {
        Master master = getMaster(assetId);
        if (hasCoinTokens(master)) {
            String publicKey = master.getPublicKey();
            String coinCurrency = getMasterCurrency(master);
            return cryptoTokenBalanceResolver.getWalletTokens(publicKey, coinCurrency);
        }
        return Collections.emptyList();
    }

    @Override
    public List<NftDto> getCoinNftTokens(String assetId) {
        Master master = getMaster(assetId);
        if (hasCoinTokens(master)) {
            String publicKey = master.getPublicKey();
            String coinCurrency = getMasterCurrency(master);
            return nftTokenLoader.loadNftsByAddress(coinCurrency, publicKey);
        } else {
            log.warn("Trying to load tokens for coin, which doesn't suppport tokens");
        }
        return Collections.emptyList();
    }

    private Master getMaster(String publicKey) {
        return masterService.findByIdOrPublicKey(publicKey)
                .orElseThrow(() -> new ObjectNotFoundException("Public key \"" + publicKey + "\" not found."));
    }

}
