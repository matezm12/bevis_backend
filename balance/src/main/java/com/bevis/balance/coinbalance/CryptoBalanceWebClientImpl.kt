package com.bevis.balance.coinbalance

import com.bevis.balance.BalanceProps
import com.bevis.balance.coinbalance.dto.Balance
import com.bevis.balance.coinbalance.dto.ErrorResponse
import com.bevis.balance.coinbalance.dto.ListDataResponse
import kotlinx.coroutines.reactive.awaitSingle
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Mono
import java.util.*
import kotlin.properties.Delegates

//"/v3/balance/{currency}/{source}/"
private const val TEMPLATE_URL = "/v3/balance/%s/%s"

private const val API_MIME_TYPE = "application/json"
private const val X_API_KEY = "x-api-key"

data class AddressesRequest(val addresses: List<String>)

@Service
@RequiredArgsConstructor
class CryptoBalanceWebClientImpl(private val balanceProps: BalanceProps) : CryptoBalanceWebClient {

    private val log = LoggerFactory.getLogger(javaClass)
    private var webClient: WebClient by Delegates.notNull()

    init {
        val baseUrl = getBaseUrl()
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, API_MIME_TYPE)
            .defaultHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT)
            .build()
        log.info("WebClient initialized with baseUrl: {}", baseUrl)
        log.trace("apiKey: {}", getApiKey())
    }

    override suspend fun loadSingleBalance(currency: String, address: String, source: String): Balance {
        val uri = "${constructUri(currency, source)}/${address}"
        log.debug("Starting... Loading single balance for {}. GET: {}", currency, getBaseUrl() + uri)
        val requestParams = hashMapOf("uri" to uri, "method" to "GET")

        val response = webClient.get().uri(uri)
            .header(X_API_KEY, getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<Balance>()
            .awaitSingle()

        return prepareResponse(response, requestParams)!!
    }

    override suspend fun loadMultiBalancesDefault(
        currency: String,
        addresses: List<String>,
        source: String
    ): ListDataResponse<Balance> {
        return loadMultiBalancesPost(currency, addresses, source)
    }

    override suspend fun loadMultiBalancesPost(
        currency: String,
        addresses: List<String>,
        source: String
    ): ListDataResponse<Balance> {
        val uri = constructUri(currency, source)
        log.debug(
            "Starting... Loading balances batch for {}. POST: {}, Addresses: {}",
            currency,
            getBaseUrl() + uri,
            addresses
        )
        val requestParams = hashMapOf("uri" to uri, "method" to "POST")

        val response = webClient.post().uri(uri)
            .header(X_API_KEY, getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(AddressesRequest(addresses)))
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<ListDataResponse<Balance>>()
            .awaitSingle()

        return prepareResponse(response, requestParams)!!
    }

    override suspend fun loadMultiBalancesGet(
        currency: String,
        addresses: List<String>,
        source: String
    ): ListDataResponse<Balance> {

        val uri = "${constructUri(currency, source)}?addresses=${addresses.joinToString(separator = ",")}"
        val requestParams = hashMapOf("uri" to uri, "method" to "GET")
        log.debug("Starting... Loading balances batch for {}. GET: {}", currency, getBaseUrl() + uri)

        val response = webClient.get().uri(uri)
            .header(X_API_KEY, getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<ListDataResponse<Balance>>()
            .awaitSingle();

        return prepareResponse(response, requestParams)!!
    }

    private fun <T> prepareResponse(response: ResponseEntity<T>, params: Map<String, String>): T? {
        val body = response.body
        log.debug("Finished successfully! {}:{}, Response: {}", params["method"], getBaseUrl() + params["uri"], body)
        return body
    }

    private fun getBaseUrl() = balanceProps.baseUrl

    private fun getApiKey() = balanceProps.apiKey

    private fun handleError(response: ClientResponse) =
        response.bodyToMono(ErrorResponse::class.java)
            .flatMap { t -> Mono.error<Throwable>(BalanceException(t.message, t.statusCode)) }

    private fun constructUri(currency: String, source: String) =
        TEMPLATE_URL.format(currency.lowercase(Locale.getDefault()), source)
}
