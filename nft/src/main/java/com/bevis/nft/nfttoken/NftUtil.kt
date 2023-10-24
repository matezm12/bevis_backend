package com.bevis.nft.nfttoken

import com.bevis.nft.nfttoken.dto.NftDto
import com.bevis.nft.nfttoken.dto.NftResponse
import com.google.common.base.Strings

fun nftDto(nftResponse: NftResponse) = NftDto(
    symbol = nftResponse.symbol ?: "",
    tokenAddress = nftResponse.tokenAddress ?: "",
    tokenId = nftResponse.tokenId ?: "",
    name = nftResponse.name ?: "",
    description = nftResponse.description ?: "",
    fileUrl = if (nftResponse.fileUrl != null) mapIpfsUrl(nftResponse.fileUrl!!) else "",
    collection = nftResponse.collection ?: "",
    amount = nftResponse.amount ?: "",
    marketUrl = nftResponse.marketUrl ?: "",
    blockchainUrl = nftResponse.blockchainUrl ?: "",
)

fun mapIpfsUrl(ipfsUrl: String): String {
    if (Strings.isNullOrEmpty(ipfsUrl)) {
        return ipfsUrl
    }
    return if (ipfsUrl.startsWith("ipfs://ipfs/")) {
        ipfsUrl.replace("ipfs://ipfs/", "https://ipfs.io/ipfs/")
    } else if (ipfsUrl.startsWith("ipfs://")) {
        ipfsUrl.replace("ipfs://", "https://ipfs.io/ipfs/")
    } else {
        ipfsUrl
    }
}
