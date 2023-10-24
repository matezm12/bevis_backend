package com.bevis.balance.token

import com.bevis.balance.dto.CryptoToken

interface CryptoTokenBalanceResolver {
    fun getWalletTokens(walletId: String, currency: String): List<CryptoToken>
}
