package com.bevis.nft.slp

import com.bevis.nft.slp.dto.Balance
import com.bevis.nft.slp.dto.SendTokensRequest
import com.bevis.nft.slp.dto.SendTokensResponse
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
private class ElectronCashSlpProxyServiceImpl(private val client: ElectronCashSlpApiWebClient) : ElectronCashSlpProxyService {


    override fun getBalance(): Balance = runBlocking  {
        return@runBlocking client.getBalance()
    }

    override fun sendTokens(request: SendTokensRequest): SendTokensResponse = runBlocking {
        return@runBlocking client.sendTokens(request)
    }
}
