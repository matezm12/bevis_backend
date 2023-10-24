package com.bevis.nft.slp

import com.bevis.nft.slp.dto.*
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import java.util.*

class ElectronCashSlpApiWebClientMock : ElectronCashSlpApiWebClient {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun getBalance(): Balance {
        delay(1000)
        return Balance(confirmed = 0.123)
    }

    override suspend fun sendTokens(request: SendTokensRequest): SendTokensResponse {
        log.debug("Mock Sending tokens request started...")
        delay(6000L + Random().nextInt(25000))
        log.debug("Mock Sending tokens request finished...")

        val rand = Random().nextInt(15000)
        if (rand > 10000) {
            throw RuntimeException("Unexpected exception...")
        }

        return SendTokensResponse(
            tokenId = request.tokenId,
            txChunks = request.outputs!!.chunked(18)
                .map { x -> ResponseChunk(
                    addresses = x,
                    tx = TransactionResponse(
                        txId = UUID.randomUUID().toString()
                    )
                ) }
        )
    }
}
