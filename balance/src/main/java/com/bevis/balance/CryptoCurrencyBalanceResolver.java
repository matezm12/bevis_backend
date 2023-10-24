package com.bevis.balance;

import com.bevis.balance.dto.Balance;
import com.bevis.balance.dto.WalletRequest;

import java.util.List;

public interface CryptoCurrencyBalanceResolver {
    /**
     * Loading from AWS lambda proxy
     * @param wallets
     * @return
     */
    List<Balance> getWalletBalances(List<WalletRequest> wallets);

    List<Balance> getWalletBalances(String currency, List<String> addresses, String source);
}
