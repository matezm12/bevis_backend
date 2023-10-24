package com.bevis.nftcore.nfttoken

import com.bevis.common.util.pmap
import com.bevis.nft.nfttoken.NftWebClient
import com.bevis.nft.nfttoken.dto.NftDataResponse
import com.bevis.nft.nfttoken.dto.NftDto
import com.bevis.nft.nfttoken.dto.NftResponse
import com.bevis.nft.nfttoken.dto.Page
import com.bevis.nft.nfttoken.nftDto
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class NftTokenLoaderImpl(
    private val nftTokenService: NftTokenService,
    private val nftWebClient: NftWebClient,
    private val tokenResponseMapper: TokenResponseMapper
): NftTokenLoader {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun loadNftsByAddress(chain: String, address: String): List<NftDto> = runBlocking {
        return@runBlocking loadNftsByAddressAsync(chain, address)
    }

    private suspend fun loadNftsByAddressAsync(chain: String, address: String): List<NftDto> {
        val response = loadNftsByAddressFromApi(chain, address)
        val nfts = if (response.shortInfo) {
            response.data.filter { it.nft }.pmap { loadNftDetails(chain, it.assetId!!) }
                .filter { it.assetId != null }
        } else {
            response.data
        }
        return nfts.map { nftDto(it) }
    }

    private  suspend fun loadNftDetails(chain: String, token: String): NftResponse {
        val nftToken = nftTokenService.findById(token)
        return if (nftToken.isPresent) {
            tokenResponseMapper.mapResponse(nftToken.get())
        } else {
            val response = loadNftDetailsFromApi(chain, token)
            val newNftToken = nftTokenService.createOrUpdate(tokenResponseMapper.mapEntity(chain, response))
            tokenResponseMapper.mapResponse(newNftToken)
        }
    }

    private suspend fun loadNftsByAddressFromApi(chain: String, address: String): NftDataResponse {
        return try {
            nftWebClient.loadNftsByAddress(chain, address, Page(page = 0, pageSize = 500))
        } catch (e: Exception) {
            log.error("Error loading NFTs. Chain: $chain, address: $address, reason: ${e.message}")
            NftDataResponse()
        }
    }

    private suspend fun loadNftDetailsFromApi(chain: String, token: String): NftResponse {
        return try {
            nftWebClient.loadNftDetails(chain, token)
        } catch (e: Exception) {
            log.error("Error loading NFT details. Chain: $chain, token: $token, reason: ${e.message}")
            NftResponse()
        }
    }
}
