package com.bevis.balancecore;

import com.bevis.balance.dto.Balance;
import com.bevis.balance.dto.WalletRequest;
import com.bevis.balancecore.domain.CoinBalance;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public final class BalanceUtil {

    public static final int BALANCE_EXPIRATION_IN_SECONDS = 300;

    public static List<WalletRequest> findOutOfDatedWallets(List<WalletRequest> updateWalletsRequest,
                                                            Map<String, CoinBalance> localCoinBalances) {
        return updateWalletsRequest.stream()
                .filter(x -> shouldUpdateBalance(localCoinBalances.get(x.getAddress())))
                .collect(Collectors.toList());
    }

    public static boolean shouldUpdateBalance(CoinBalance coinBalance) {
        return Objects.isNull(coinBalance) || Objects.isNull(coinBalance.getBalance()) ||
                Instant.now().minusSeconds(BALANCE_EXPIRATION_IN_SECONDS).isAfter(coinBalance.getUpdatedAt());
    }

    public static CoinBalance newCoinBalance(String address, String currency) {
        CoinBalance coinBalance = new CoinBalance();
        coinBalance.setPublicKey(address);
        coinBalance.setCurrency(currency);
        coinBalance.setCreatedAt(Instant.now());
        coinBalance.setUpdatedAt(Instant.now());
        return coinBalance;
    }

    public static List<Balance> mapBalances(Collection<CoinBalance> coinBalance) {
        if (Objects.isNull(coinBalance) || coinBalance.isEmpty()) {
            return Collections.emptyList();
        }
        return coinBalance.stream()
                .map(BalanceUtil::mapBalance)
                .collect(Collectors.toList());
    }

    public static Balance mapBalance(CoinBalance coinBalance) {
        return Balance.builder()
                .address(coinBalance.getPublicKey())
                .currency(coinBalance.getCurrency())
                .value(coinBalance.getBalance())
                .divider(coinBalance.getDivider())
                .build();
    }

}
