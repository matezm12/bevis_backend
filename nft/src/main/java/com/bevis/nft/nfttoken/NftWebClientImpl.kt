package com.bevis.nft.nfttoken

import com.bevis.nft.nfttoken.dto.ErrorResponse
import com.bevis.nft.nfttoken.dto.NftDataResponse
import com.bevis.nft.nfttoken.dto.NftResponse
import com.bevis.nft.nfttoken.dto.Page
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Mono
import kotlin.properties.Delegates


private const val API_MIME_TYPE = "application/json"
private const val X_API_KEY = "x-api-key"

@Service
class NftWebClientImpl(private val nftGatewayProps: NftGatewayProps) : NftWebClient {

    private val log = LoggerFactory.getLogger(javaClass)
    private var webClient: WebClient by Delegates.notNull()

    init {
        val baseUrl = getBaseUrl()
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, API_MIME_TYPE)
            .defaultHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT)
            .build()
        log.info("Nft WebClient initialized with baseUrl: $baseUrl")
        log.trace("apiKey: ${getApiKey()}")
    }

    override suspend fun loadNftsByAddress(chain: String, address: String, page: Page): NftDataResponse {
        val uri = "/v2/${chain.lowercase()}/$address/nft?page=${page.page}&pageSize=${page.pageSize}"
        log.debug("Starting... Loading NFTs for chain: $chain, wallet: $address. GET: ${getBaseUrl() + uri}")

        val response = webClient.get().uri(uri)
            .header(X_API_KEY, getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<NftDataResponse>()
            .awaitSingle()

        val nftDataResponse = response.body
        log.debug("Finished successfully! NFTs loaded URL : ${getBaseUrl() + uri} , Response: $nftDataResponse")
        return nftDataResponse!!
    }

    override suspend fun loadNftDetails(chain: String, token: String): NftResponse {
        val uri = "/v2/${chain.lowercase()}/nft/$token"
        log.debug("Starting... Loading NFT details for chain: $chain, token: $token. GET: ${getBaseUrl() + uri}")

        val response = webClient.get().uri(uri)
            .header(X_API_KEY, getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<NftResponse>()
            .awaitSingle()

        val nftDataResponse = response.body
        log.debug("Finished successfully! NFT details loaded ${getBaseUrl() + uri}:{}, Response: $nftDataResponse")
        return nftDataResponse!!
    }

    private fun handleError(response: ClientResponse) =
        response.bodyToMono(ErrorResponse::class.java)
            .flatMap { t -> Mono.error<Throwable>(NftException(t.message, t.statusCode)) }

    private fun getBaseUrl() = nftGatewayProps.baseUrl

    private fun getApiKey() = nftGatewayProps.apiKey

}
