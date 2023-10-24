package com.bevis.appbe.web.rest.cscapp;


import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.appbe.web.rest.vm.MessageResponseVM;
import com.bevis.balance.dto.CryptoToken;
import com.bevis.nft.nfttoken.dto.NftDto;
import com.bevis.coininfo.CoinDetailsService;
import com.bevis.coininfo.CoinInfoService;
import com.bevis.coininfo.CoinActivationService;
import com.bevis.coininfo.CoinTokensService;
import com.bevis.coininfo.dto.*;
import com.bevis.common.dto.DataPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CoinInfoController {
    private final CoinTokensService coinTokensService;
    private final CoinInfoService coinInfoService;
    private final CoinDetailsService coinDetailsService;
    private final CoinActivationService coinActivationService;

    @GetMapping("/coin-tokens")
    DataPage<CryptoToken> getCoinTokens(@RequestParam("public_key") String publicKey) {
        log.debug("REST request to get Coin tokens by public key : {}", publicKey);
        return DataPage.of(coinTokensService.getCoinTokens(publicKey));
    }

    @GetMapping("/coin-tokens/nft")
    DataPage<NftDto> getCoinNftTokens(@RequestParam("public_key") String publicKey) {
        log.debug("REST request to get Coin tokens by public key : {}", publicKey);
        return DataPage.of(coinTokensService.getCoinNftTokens(publicKey));
    }

    @Deprecated
    @GetMapping("/v3/coins")
    DataResponse<Coin> loadCoinAssetsInfo(@RequestParam("coin_ids") List<String> publicKeys,
                                          @RequestParam(value = "fiat-currency", defaultValue = "USD") String fiatCurrency) {
        log.debug("REST request to get Coin by public keys : {} and currency : {}", publicKeys, fiatCurrency);
        List<CoinRequest> coinsRequest = publicKeys.stream()
                .map(x->new CoinRequest(x, null)).collect(Collectors.toList());
        CryptoCoinsInfo cryptoCoinsInfo = coinInfoService.loadCoinAssetsInfo(coinsRequest, fiatCurrency);
        return DataResponse.of(cryptoCoinsInfo.getCoins(), cryptoCoinsInfo.getMetadata());
    }

    @PostMapping("/v4/coins")
    DataResponse<Coin> loadCoinAssetsInfoV4(@RequestBody List<CoinRequest> coins,
                                          @RequestParam(value = "fiat-currency", defaultValue = "USD") String fiatCurrency) {
        log.debug("REST request to get Coin by public keys : {} and currency : {}", coins, fiatCurrency);
        CryptoCoinsInfo cryptoCoinsInfo = coinInfoService.loadCoinAssetsInfo(coins, fiatCurrency);
        return DataResponse.of(cryptoCoinsInfo.getCoins(), cryptoCoinsInfo.getMetadata());
    }

    @Deprecated
    @GetMapping("/v3/coin-details")
    CoinDetails getCoinDetails(@RequestParam("coin_id") String coinId,
                               @RequestParam(value = "fiat-currency", defaultValue = "USD") String fiatCurrency) {
        log.debug("REST request to get Coin details by coinId : {} ", coinId);
        return coinDetailsService.getCoinDetails(new CoinRequest(coinId, null), fiatCurrency);
    }

    @PostMapping("/v4/coin-details")
    CoinDetails getCoinDetailsV2(@RequestBody CoinRequest coin,
                               @RequestParam(value = "fiat-currency", defaultValue = "USD") String fiatCurrency) {
        log.debug("REST request to get Coin details by coinId : {} ", coin);
        return coinDetailsService.getCoinDetails(coin, fiatCurrency);
    }

    @PostMapping("/v4/coins/activate")
    MessageResponseVM activateCoin(@RequestBody CoinActivationRequest coinActivationRequest) {
        log.debug("REST request to activate the Coin : {} ", coinActivationRequest);
        coinActivationService.activateCoin(coinActivationRequest);
        return MessageResponseVM.builder()
                .message("Activation success")
                .build();
    }

    @PostMapping("/v4/coins/deactivate")
    MessageResponseVM deactivateCoin(@RequestBody CoinActivationRequest coinActivationRequest) {
        log.debug("REST request to activate the Coin : {} ", coinActivationRequest);
        coinActivationService.deactivateCoin(coinActivationRequest);
        return MessageResponseVM.builder()
                .message("Deactivation success")
                .build();
    }
}
