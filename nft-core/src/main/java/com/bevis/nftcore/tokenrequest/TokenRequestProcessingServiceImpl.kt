package com.bevis.nftcore.tokenrequest


import com.bevis.nftcore.domain.TokenRequest
import com.bevis.nftcore.domain.TokenTransfer
import com.bevis.nftcore.domain.enumeration.TokenRequestStatus
import com.bevis.nft.slp.ElectronCashSlpApiWebClient
import com.bevis.nft.slp.dto.SendTokensRequest
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
open class TokenRequestProcessingServiceImpl(
    private val tokenRequestService: TokenRequestService,
    private val tokenTransferService: TokenTransferService,
    private val electronCashSlpApiWebClient: ElectronCashSlpApiWebClient,
    private val props: TokenRequestProps
) : TokenRequestProcessingService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override suspend fun processNewTokenRequest(tokenRequestId: Long) {
        val tokenRequest = tokenRequestService.findById(tokenRequestId).orElseThrow()
        val tokenTransfers = tokenTransferService.findAllByRequestId(tokenRequestId)
        processTokenRequest(tokenRequest, tokenTransfers)
    }

    @Transactional
    override suspend fun retryFailedTransfers(tokenRequestId: Long) {
        val tokenRequest = tokenRequestService.findById(tokenRequestId).orElseThrow()
        val tokenTransfers = tokenTransferService.findAllFailedByRequestId(tokenRequestId)
        processTokenRequest(tokenRequest, tokenTransfers)
    }

    private suspend fun processTokenRequest(tokenRequest: TokenRequest, tokenTransfers: List<TokenTransfer>) {

        if (!tokenTransfers.all { it.tokenRequest == tokenRequest }) {
            throw RuntimeException("Error token request mismatch")
        }

        val tokenTransfersChunks = tokenTransfers.chunked(props.chunkSize)

        val updatedTransfers = processTransfersChunks(tokenRequest, tokenTransfersChunks)

        if (updatedTransfers.all { it.transactionId != null }) {
            tokenRequest.status = TokenRequestStatus.PROCESSED
        }
        if (updatedTransfers.any { it.errorMessage != null }) {
            tokenRequest.status = TokenRequestStatus.FAILED
        }
        tokenRequestService.createOrUpdate(tokenRequest)
    }

    private suspend fun processTransfersChunks(tokenRequest: TokenRequest, tokenTransfersChunks: List<List<TokenTransfer>>): List<TokenTransfer> {
        val res: MutableList<TokenTransfer> = ArrayList()
        for (chunk in tokenTransfersChunks) {
            val processedChunk = processTransfersChunk(tokenRequest, chunk, tokenTransfersChunks.indexOf(chunk))
            delay(100)
            res.addAll(processedChunk)
        }
        return res
    }

    private suspend fun processTransfersChunk(
        tokenRequest: TokenRequest,
        tokenTransfers: List<TokenTransfer>,
        chunkIndex: Int
    ): List<TokenTransfer> {
        log.debug("Start processing chunk $chunkIndex...")
        if (!tokenTransfers.all { it.tokenRequest == tokenRequest }) {
            throw RuntimeException("Error token request mismatch")
        }
        try {
            val transactionsResult = electronCashSlpApiWebClient.sendTokens(SendTokensRequest(
                tokenId = tokenRequest.tokenId,
                outputs = tokenTransfers.map { it.destinationAddress }
            ))
            for (txChunk in transactionsResult.txChunks!!) {
                val tx = txChunk.tx
                val addresses = txChunk.addresses!!.toSet()
                tokenTransfers
                    .filter { addresses.contains(it.destinationAddress) }
                    .forEach {
                        it.transactionId = tx!!.txId
                        it.errorMessage = null
                    }
            }
        } catch (e: Exception) {
            tokenTransfers.forEach { it.errorMessage = e.message }
        }
        tokenTransfers.forEach { it.updatedAt = Instant.now() }
        val updatedTransfers = tokenTransferService.saveAll(tokenTransfers)
        log.debug("Successfully processed chunk $chunkIndex!")
        return updatedTransfers
    }

}
