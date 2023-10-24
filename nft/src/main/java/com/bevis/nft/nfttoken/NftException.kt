package com.bevis.nft.nfttoken

data class NftException(override val message: String, val statusCode: Int? = null) : Exception(message)

