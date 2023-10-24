package com.bevis.nft.nfttoken.dto

data class NftDto(
    var symbol: String = "",
    var tokenAddress: String = "",
    var tokenId: String = "",
    var name: String = "",
    var description: String = "",
    var fileUrl: String = "",
    var collection: String = "",
    var amount: String = "",
    var marketUrl: String = "",
    var blockchainUrl: String = "",
)
