package com.bevis.nftcore.tokenrequest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class TokenRequestProcessingProxyServiceImpl(
    private val tokenRequestProcessingService: TokenRequestProcessingService
) : TokenRequestProcessingProxyService {

    override fun processTokenRequest(tokenRequestId: Long) = runBlocking(Dispatchers.IO) {
        tokenRequestProcessingService.processNewTokenRequest(tokenRequestId)
    }

    override fun retryFailedTransfers(tokenRequestId: Long) = runBlocking(Dispatchers.IO) {
        tokenRequestProcessingService.retryFailedTransfers(tokenRequestId)
    }
}
