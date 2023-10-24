package com.bevis.nft.nfttoken.dto

data class NftResponse(
    var assetId: String? = null,
    var symbol: String? = null,
    var tokenAddress: String? = null,
    var tokenId: String? = null,
    var name: String? = null,
    var description: String? = null,
    var fileUrl: String? = null,
    var collection: String? = "",
    var amount: String? = "",
    var marketUrl: String? = "",
    var blockchainUrl: String? = "",
    var nft: Boolean = false
)
