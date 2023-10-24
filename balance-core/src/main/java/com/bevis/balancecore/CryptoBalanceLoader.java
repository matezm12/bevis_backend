package com.bevis.balancecore;

import com.bevis.balance.dto.Balance;
import com.bevis.balance.dto.WalletRequest;

import java.util.List;

public interface CryptoBalanceLoader {
    Balance getWalletBalance(String walletId, String currency);

    List<Balance> getWalletBalances(List<WalletRequest> walletsToLoad);
}
