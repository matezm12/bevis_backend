package com.bevis.balance.token

import com.bevis.balance.dto.CryptoToken
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class CryptoTokenBalanceResolverImpl(private val erc20TokenService: Erc20TokenService) : CryptoTokenBalanceResolver {

    override fun getWalletTokens(walletId: String, currency: String): List<CryptoToken> = runBlocking {
        return@runBlocking erc20TokenService.getWalletTokens(walletId, currency)
    }
}
