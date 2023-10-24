package com.bevis.nftcore.tokenrequest

interface TokenRequestProcessingService {
    suspend fun processNewTokenRequest(tokenRequestId: Long)
    suspend fun retryFailedTransfers(tokenRequestId: Long)
}
