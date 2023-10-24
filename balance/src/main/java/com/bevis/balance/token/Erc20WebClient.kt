package com.bevis.balance.token

import com.bevis.balance.token.dto.Erc20DataResponse
import com.bevis.balance.token.dto.Page


interface Erc20WebClient {
    suspend fun loadTokensByAddress(chain: String, address: String, page: Page): Erc20DataResponse
}
