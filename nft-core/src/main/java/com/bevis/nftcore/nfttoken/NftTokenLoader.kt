package com.bevis.nftcore.nfttoken

import com.bevis.nft.nfttoken.dto.NftDto

interface NftTokenLoader {
    fun loadNftsByAddress(chain: String, address: String): List<NftDto>
}
