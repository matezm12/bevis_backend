package com.bevis.nft.slp

import com.bevis.nft.slp.dto.Balance
import com.bevis.nft.slp.dto.SendTokensRequest
import com.bevis.nft.slp.dto.SendTokensResponse

interface ElectronCashSlpProxyService {
    fun getBalance(): Balance
    fun sendTokens(request: SendTokensRequest): SendTokensResponse
}
