package com.bevis.balance.token

import com.bevis.balance.dto.CryptoToken

interface Erc20TokenService {
    suspend fun getWalletTokens(walletId: String, currency: String): List<CryptoToken>
}
