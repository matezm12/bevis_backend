package com.bevis.nft.nfttoken.dto

data class NftDataResponse(var shortInfo: Boolean = false, var data: List<NftResponse> = emptyList())
