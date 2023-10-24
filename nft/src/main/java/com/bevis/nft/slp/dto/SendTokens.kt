package com.bevis.nft.slp.dto

data class SendTokensRequest(
    var tokenId: String? = null,
    var outputs: List<String>? = null
)

data class SendTokensResponse(
    var tokenId: String? = null,
    var txChunks: List<ResponseChunk>? = null
)

data class ResponseChunk(
    var addresses: List<String>? = null,
    var tx: TransactionResponse? = null
)

data class TransactionResponse(var txId: String? = null)
