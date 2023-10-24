package com.bevis.balance

import com.bevis.balance.coinbalance.dto.Balance
import com.bevis.balance.coinbalance.dto.WalletRequest

interface AsyncCryptoCurrencyBalanceResolver {
    fun loadSingleBalance(address: String, currency: String): Balance
    fun loadBalances(wallets: List<WalletRequest>): List<Balance?>
    fun loadBalances(currency: String, addresses: List<String>, source: String): List<Balance?>
}
