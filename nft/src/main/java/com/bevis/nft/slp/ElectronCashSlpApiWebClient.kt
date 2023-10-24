package com.bevis.nft.slp

import com.bevis.nft.slp.dto.Balance
import com.bevis.nft.slp.dto.SendTokensRequest
import com.bevis.nft.slp.dto.SendTokensResponse

interface ElectronCashSlpApiWebClient {
    suspend fun getBalance(): Balance
    suspend fun sendTokens(request: SendTokensRequest): SendTokensResponse
}
