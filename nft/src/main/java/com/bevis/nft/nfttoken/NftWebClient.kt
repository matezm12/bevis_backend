package com.bevis.nft.nfttoken

import com.bevis.nft.nfttoken.dto.NftDataResponse
import com.bevis.nft.nfttoken.dto.NftResponse
import com.bevis.nft.nfttoken.dto.Page

interface NftWebClient {
    suspend fun loadNftsByAddress(chain: String, address: String, page: Page): NftDataResponse
    suspend fun loadNftDetails(chain: String, token: String): NftResponse
}
