package com.bevis.nftcore.tokenrequest

interface TokenRequestProcessingProxyService {
    fun processTokenRequest(tokenRequestId: Long)
    fun retryFailedTransfers(tokenRequestId: Long)
}
