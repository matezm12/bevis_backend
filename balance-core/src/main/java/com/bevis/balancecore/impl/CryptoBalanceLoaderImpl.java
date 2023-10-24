package com.bevis.balancecore.impl;

import com.bevis.balance.CryptoCurrencyBalanceResolver;
import com.bevis.balance.dto.Balance;
import com.bevis.balance.dto.WalletRequest;
import com.bevis.balancecore.BalanceUtil;
import com.bevis.balancecore.CoinBalanceService;
import com.bevis.balancecore.CryptoBalanceLoader;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.balancecore.domain.CoinBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bevis.balancecore.BalanceUtil.*;

@Transactional(noRollbackFor = Exception.class)
@Slf4j
@Service
@RequiredArgsConstructor
class CryptoBalanceLoaderImpl implements CryptoBalanceLoader {

    private final CryptoCurrencyBalanceResolver cryptoCurrencyBalanceResolver;
    private final CoinBalanceService coinBalanceService;

    @Override
    public Balance getWalletBalance(String publicKey, String currency) {
        WalletRequest walletRequest = WalletRequest.builder()
                .address(publicKey)
                .currency(currency)
                .build();
        return getWalletBalances(Collections.singletonList(walletRequest))
                .stream().findFirst()
                .orElseThrow(ObjectNotFoundException::new);
    }

    @Override
    public List<Balance> getWalletBalances(List<WalletRequest> updateWalletsRequest) {
        //Map key is a wallet address (public key)
        final Map<String, CoinBalance> coinBalances = loadCoinBalances(updateWalletsRequest);
        log.trace("Prepared coin balances Map: {}", coinBalances);

        try {
            //List of wallets, needed to update by service
            List<WalletRequest> walletsForUpdate = findOutOfDatedWallets(updateWalletsRequest, coinBalances);
            log.trace("Loaded list of wallet, which are needed to be updated: {}", walletsForUpdate);

            //"Update procedure" returns a new map of updated wallets
            Map<String, CoinBalance> updatedCoinBalances = updateBalances(walletsForUpdate, coinBalances);
            log.trace("Loaded map of balances updated with third-party service: {}", updatedCoinBalances);

            coinBalances.putAll(updatedCoinBalances);
            saveCoinBalances(coinBalances);
            log.debug("Updated balances successfully saved in database");

            return updateWalletsRequest.stream()
                    .map(walletRequest -> coinBalances.getOrDefault(walletRequest.getAddress(), null)) //Map CoinBalance
                    .filter(Objects::nonNull)
                    .map(BalanceUtil::mapBalance)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Cannot load balance from service! Trying to load cached balance from database...");
            return mapBalances(coinBalances.values());
        }
    }

    private Map<String, CoinBalance> loadCoinBalances(List<WalletRequest> updateWalletsRequest) {
        return updateWalletsRequest.stream()
                .map(walletRequest -> coinBalanceService.findCoinByAddress(walletRequest.getAddress()).orElse(null))
                .filter(Objects::nonNull)
                .peek(coinBalance -> coinBalance.setLastScanned(Instant.now()))
                .collect(Collectors.toMap(CoinBalance::getPublicKey, Function.identity()));
    }

    private Map<String, CoinBalance> updateBalances(List<WalletRequest> updateWalletsRequest,
                                                    Map<String, CoinBalance> localCoinBalances) {

        final Map<String, CoinBalance> updatedCoinBalances = new HashMap<>();
        if (!updateWalletsRequest.isEmpty()) {
            final List<Balance> updatedWallets = cryptoCurrencyBalanceResolver.getWalletBalances(updateWalletsRequest);
            log.debug("Coin balance successfully loaded from third-party api.");
            log.trace("Updated balances: {}", updatedWallets);
            updatedWallets.forEach(balance -> {
                String address = balance.getAddress();
                CoinBalance coinBalance = localCoinBalances
                        .getOrDefault(address, newCoinBalance(address, balance.getCurrency()));
                coinBalance.setBalance(balance.getValue());
                coinBalance.setDivider(balance.getDivider());
                coinBalance.setUpdatedAt(Instant.now());
                coinBalance.setLastScanned(Instant.now());
                updatedCoinBalances.put(address, coinBalance);
            });
        } else {
            log.debug("None of balance needed to update...");
        }
        return updatedCoinBalances;
    }

    private void saveCoinBalances(Map<String, CoinBalance> coinBalances) {
        coinBalanceService.saveAll(coinBalances.values());
    }
}
