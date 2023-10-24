package com.bevis.balance.coinbalance

import com.bevis.balance.coinbalance.dto.Balance
import com.bevis.balance.coinbalance.dto.ListDataResponse

interface CryptoBalanceWebClient {
    suspend fun loadSingleBalance(currency: String, address: String, source: String): Balance
    suspend fun loadMultiBalancesDefault(currency: String, addresses: List<String>, source: String): ListDataResponse<Balance>
    suspend fun loadMultiBalancesPost(currency: String, addresses: List<String>, source: String): ListDataResponse<Balance>
    suspend fun loadMultiBalancesGet(currency: String, addresses: List<String>, source: String): ListDataResponse<Balance>
}
