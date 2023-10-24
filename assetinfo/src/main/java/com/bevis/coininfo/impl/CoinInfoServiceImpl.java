package com.bevis.coininfo.impl;

import com.bevis.assetinfo.LogoFetchingService;
import com.bevis.assetinfo.mapper.AssetInfoMapper;
import com.bevis.balance.dto.Balance;
import com.bevis.balance.dto.WalletRequest;
import com.bevis.balancecore.CryptoBalanceLoader;
import com.bevis.coininfo.CoinInfoService;
import com.bevis.coininfo.CscMasterLoader;
import com.bevis.coininfo.dto.Coin;
import com.bevis.coininfo.dto.CoinRequest;
import com.bevis.coininfo.dto.CryptoCoinsInfo;
import com.bevis.coininfo.dto.CryptoCoinsMetadata;
import com.bevis.master.domain.Master;
import com.bevis.exchange.dto.CurrencyExchangeRate;
import com.bevis.exchangedata.CurrencyExchangeLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.assetinfo.util.MasterFetchUtil.*;

@Service
@Slf4j
@RequiredArgsConstructor
class CoinInfoServiceImpl implements CoinInfoService {

    private final CscMasterLoader cscMasterLoader;
    private final CurrencyExchangeLoader currencyExchangeLoader;
    private final LogoFetchingService logoFetchingService;
    private final CryptoBalanceLoader cryptoBalanceLoader;
    private final AssetInfoMapper assetInfoMapper;

    @Override
    public CryptoCoinsInfo loadCoinAssetsInfo(List<CoinRequest> coinsRequest, String fiatCurrency) {
        log.trace("Loading coin assets info");
        Map<String, Double> keys = coinsRequest.stream()
                .filter(x->Objects.nonNull(x.getCryptoBalance()))
                .collect(HashMap::new, (m,v)->m.put(v.getPublicKey(), v.getCryptoBalance()), HashMap::putAll);
        List<String> publicKeys = coinsRequest.stream()
                .map(CoinRequest::getPublicKey)
                .distinct()
                .collect(Collectors.toList());
        List<Master> masters = getAllByPublicKeys(publicKeys);
        log.trace("Loaded masters");

        Map<String, Double> updatedKeys = loadBalancesForMastersNotInKeys(masters, keys);

        keys.putAll(updatedKeys);

        List<Coin> coins = masters.stream()
                .map(master -> loadCoin(master, fiatCurrency, keys.get(master.getPublicKey())))
                .collect(Collectors.toList());
        log.trace("Loaded coins with balances");

        CryptoCoinsMetadata metadata = new CryptoCoinsMetadata();
        metadata.setFiatCurrency(fiatCurrency);
        metadata.setTotalFiatBalance(calculateTotalFiatBalance(coins));
        return CryptoCoinsInfo.of(coins, metadata);
    }

    @Override
    public Coin loadCoin(@NonNull Master master, @NonNull String fiatCurrency, Double manualBalance) {
        log.trace("Loading coin: {}, fiat currency: {}", master.getPublicKey(), fiatCurrency);
        String cryptoCurrency = getMasterCurrency(master);
        String currencyCode = getMasterCurrencyCode(master);
        Coin coin = new Coin();
        coin.setAssetId(master.getId());
        coin.setPublicKey(master.getPublicKey());
        coin.setLogo(logoFetchingService.loadFileUrlByMaster(master));
        coin.setPublicKeyUrl(assetInfoMapper.getBlockchainAddressLink(master));
        coin.setZone1(master.getId());
        coin.setActivationStatus(master.getActivationStatus());

        Optional.of(master)
                .map(Master::getBlockchain)
                .ifPresent(blockchain -> {
                    coin.setPrivateBalance(blockchain.isPrivateBalance());
                    coin.setBlockchainCode(blockchain.getName());
                    coin.setBlockchainName(blockchain.getFullName());
                });

        coin.setHasTokens(hasCoinTokens(master));
        if (Objects.nonNull(manualBalance)){
            coin.setCryptoBalance(manualBalance);
        }
        coin.setBalanceLoaded(Objects.nonNull(coin.getCryptoBalance()));
        if (coin.isBalanceLoaded()) {
            coin.setFiatBalance(loadFiatBalance(currencyCode, coin.getCryptoBalance(), fiatCurrency));
        }

        if (coin.isBalanceLoaded()) {
            coin.setZone2(formatCryptoBalance(currencyCode, coin.getCryptoBalance()));
            coin.setZone3(formatFiatBalance(fiatCurrency, coin.getFiatBalance()));
        } else {
            coin.setZone2(cryptoCurrency);
            coin.setZone3(fiatCurrency);
        }

        return coin;
    }

    private Map<String, Double> loadBalancesForMastersNotInKeys(List<Master> masters, Map<String, Double> keys) {
        Map<String, Double> updatedKeys = new HashMap<>();
        List<WalletRequest> walletsToLoad = masters.stream()
                .filter(x -> Objects.nonNull(x.getBlockchain()) && !keys.containsKey(x.getPublicKey()))
                .filter(x -> !x.getBlockchain().isPrivateBalance())
                .filter(x -> x.getBlockchain().isBalanceEnable())
                .map(x -> WalletRequest.builder()
                        .address(x.getPublicKey())
                        .currency(x.getBlockchain().getName())
                        .build())
                .collect(Collectors.toList());
        List<Balance> walletBalances;
        try {
            walletBalances = cryptoBalanceLoader.getWalletBalances(walletsToLoad);
        } catch (Exception e) {
            walletBalances = Collections.emptyList();
            log.error("Error loading balances with cause: {}", e.getMessage());
        }
        walletBalances
                .stream()
                .filter(x-> Objects.nonNull(x.getValue()))
                .forEach(x->updatedKeys.put(x.getAddress(), Balance.getRealBalance(x)));
        return updatedKeys;
    }

    private List<Master> getAllByPublicKeys(List<String> publicKeys) {
        return cscMasterLoader.findAllCscMastersByPublicKeys(publicKeys);
    }

    private Double calculateTotalFiatBalance(List<Coin> coins) {
        log.trace("Calculating the summary of all coins in fiat currency");
        return coins.stream()
                .filter(x -> Objects.nonNull(x.getFiatBalance()))
                .mapToDouble(Coin::getFiatBalance)
                .sum();
    }

    private String formatFiatBalance(String fiatCurrency, Double fiatBalance) {
        if (Objects.isNull(fiatBalance)) {
            log.warn("Could not format fiat balance. fiatCurrency: {}, fiatBalance: {}", fiatCurrency, fiatBalance);
            if (Objects.nonNull(fiatCurrency)) {
                return String.format("%s N/A", fiatCurrency.toUpperCase());
            }
            return "N/A";
        }
        return String.format("%s %4.2f", fiatCurrency.toUpperCase(), fiatBalance);
    }

    private Double loadFiatBalance(String sourceCurrency, Double cryptoBalance, String fiatCurrency) {
        if (Objects.isNull(sourceCurrency) || Objects.isNull(cryptoBalance) || Objects.isNull(fiatCurrency)) {
            log.warn("Could not load fiat balance. sourceCurrency: {}, cryptoBalance: {}, fiatCurrency: {}", sourceCurrency, cryptoBalance, fiatCurrency);
            return null;
        }
        try {
            CurrencyExchangeRate exchangeRate = currencyExchangeLoader.getExchangeRateForCurrencies(sourceCurrency, fiatCurrency);
            if (Objects.isNull(exchangeRate) || Objects.isNull(exchangeRate.getValue())) {
                log.warn("ExchangeRate is null. Crypto currency: {}, Balance: {}, Fiat Currency: {}", sourceCurrency, cryptoBalance, fiatCurrency);
                return null;
            }
            Double exchangeRateValue = exchangeRate.getValue();
            return cryptoBalance * exchangeRateValue;
        } catch (Exception e) {
            log.error("Could not fiat load balance. Crypto currency: {}, Balance: {}, Fiat Currency: {}", sourceCurrency, cryptoBalance, fiatCurrency);
            log.error(e.getMessage());
            return null;
        }
    }

    private String formatCryptoBalance(String cryptoCurrency, Double cryptoBalance) {
        if (Objects.isNull(cryptoBalance)) {
            log.warn("Could not format crypto balance. cryptoCurrency: {}, cryptoBalance: {}", cryptoCurrency, cryptoBalance);
            if (Objects.nonNull(cryptoCurrency)) {
                return String.format("N/A %s", cryptoCurrency.toUpperCase());
            }
            return "N/A";
        }
        return String.format("%4.8f %s", cryptoBalance, cryptoCurrency.toUpperCase());
    }
}
