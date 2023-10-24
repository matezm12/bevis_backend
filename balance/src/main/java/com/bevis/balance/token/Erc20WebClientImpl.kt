package com.bevis.balance.token

import com.bevis.balance.token.dto.Erc20DataResponse
import com.bevis.balance.token.dto.ErrorResponse
import com.bevis.balance.token.dto.Page
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import kotlin.properties.Delegates
import reactor.core.publisher.Mono


private const val API_MIME_TYPE = "application/json"
private const val X_API_KEY = "x-api-key"

@Service
private class Erc20WebClientImpl(private val erc20TokenProps: Erc20TokenProps) : Erc20WebClient {

    private val log = LoggerFactory.getLogger(javaClass)
    private var webClient: WebClient by Delegates.notNull()

    init {
        val baseUrl = getBaseUrl()
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, API_MIME_TYPE)
            .defaultHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT)
            .build()
        log.info("Erc-20 WebClient initialized with baseUrl: $baseUrl")
        log.trace("apiKey: ${getApiKey()}")
    }

    override suspend fun loadTokensByAddress(chain: String, address: String, page: Page): Erc20DataResponse {
        val uri = "/v2/${chain.lowercase()}/$address/erc20?page=${page.page}&pageSize=${page.pageSize}"
        log.debug("Starting... Loading ERC20 for chain: $chain, wallet: $address. GET: ${getBaseUrl() + uri}")
        val response = webClient.get().uri(uri)
            .header(X_API_KEY, getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<Erc20DataResponse>()
            .awaitSingle()

        val erc20DataResponse = response.body
        log.debug("Finished successfully! ERC-20 loaded URL : ${getBaseUrl() + uri} , Response: $erc20DataResponse")
        return erc20DataResponse!!
    }

    private fun handleError(response: ClientResponse) =
        response.bodyToMono(ErrorResponse::class.java)
            .flatMap { t -> Mono.error<Throwable>(Erc20Exception(t.message, t.statusCode)) }

    private fun getBaseUrl() = erc20TokenProps.baseUrl!!

    private fun getApiKey() = erc20TokenProps.apiKey!!

}
