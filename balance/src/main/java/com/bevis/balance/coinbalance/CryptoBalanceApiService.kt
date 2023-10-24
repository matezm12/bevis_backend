package com.bevis.balance.coinbalance

import com.bevis.balance.coinbalance.dto.Balance
import com.bevis.balance.coinbalance.dto.WalletRequest

interface CryptoBalanceApiService {
    suspend fun loadSingleBalance(currency: String, address: String): Balance?
    suspend fun loadSingleBalance(currency: String, address: String, source: String): Balance?
    suspend fun loadBalances(wallets: List<WalletRequest>): List<Balance>
    suspend fun loadBalances(currency: String, addresses: List<String>, source: String): List<Balance>
    suspend fun loadBalances(currency: String, addresses: List<String>): List<Balance>
}
