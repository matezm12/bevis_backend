package com.bevis.nftcore.nfttoken

import com.bevis.nftcore.domain.NftToken
import com.bevis.nft.nfttoken.dto.NftResponse
import org.springframework.stereotype.Service

@Service
class TokenResponseMapper {

    fun mapResponse(nftToken: NftToken): NftResponse {
        return NftResponse(
            assetId = nftToken.id,
            symbol = nftToken.symbol,
            tokenAddress = nftToken.address,
            tokenId = nftToken.tokenId,
            name = nftToken.name,
            description = nftToken.description,
            fileUrl = nftToken.fileUrl,
            collection = nftToken.collection,
            marketUrl = nftToken.marketUrl,
            blockchainUrl = nftToken.blockchainUrl,
            nft = true
        )
    }

    fun mapEntity(chain: String, nftResponse: NftResponse): NftToken {
        val nftToken = NftToken()
        nftToken.id = nftResponse.assetId
        nftToken.chain = chain
        nftToken.tokenId = nftResponse.tokenId
        nftToken.symbol = nftResponse.symbol
        nftToken.address = nftResponse.tokenAddress
        nftToken.name = nftResponse.name
        nftToken.description = nftResponse.description
        nftToken.fileUrl = nftResponse.fileUrl
        nftToken.collection = nftResponse.collection
        nftToken.marketUrl = nftResponse.marketUrl
        nftToken.blockchainUrl = nftResponse.blockchainUrl
        return nftToken
    }
}
