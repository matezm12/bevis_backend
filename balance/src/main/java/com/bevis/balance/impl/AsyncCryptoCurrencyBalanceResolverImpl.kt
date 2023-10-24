package com.bevis.balance.impl

import com.bevis.balance.AsyncCryptoCurrencyBalanceResolver
import com.bevis.balance.coinbalance.CryptoBalanceApiService
import com.bevis.balance.coinbalance.dto.Balance
import com.bevis.balance.coinbalance.dto.WalletRequest
import kotlinx.coroutines.runBlocking
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AsyncCryptoCurrencyBalanceResolverImpl(
    private val cryptoBalanceApiService: CryptoBalanceApiService
) : AsyncCryptoCurrencyBalanceResolver {

    override fun loadSingleBalance(address: String, currency: String): Balance = runBlocking {
        return@runBlocking cryptoBalanceApiService.loadSingleBalance(currency = currency, address = address)!!
    }

    override fun loadBalances(wallets: List<WalletRequest>): List<Balance?> = runBlocking {
        return@runBlocking cryptoBalanceApiService.loadBalances(wallets)
    }

    override fun loadBalances(currency: String, addresses: List<String>, source: String): List<Balance?> = runBlocking {
        return@runBlocking cryptoBalanceApiService.loadBalances(currency, addresses, source)
    }
}
