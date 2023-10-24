package com.bevis.keygen

import com.bevis.common.util.concatenate
import com.bevis.common.util.pmap
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Mono
import kotlin.properties.Delegates

private const val API_MIME_TYPE = "application/json"
private const val X_API_KEY = "x-api-key"
private const val TASK_CAPACITY = 200

@Service
private class AsyncAwsLambdaCryptoKeyGeneratorServiceImpl(
    val cryptoWalletGeneratorProps: CryptoWalletGeneratorProps
) : CryptoKeyGeneratorService {

    private val log = LoggerFactory.getLogger(javaClass)
    private var webClient: WebClient by Delegates.notNull()

    init {
        val baseUrl = cryptoWalletGeneratorProps.awsLambdaUrl
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, API_MIME_TYPE)
            .defaultHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT)
            .build()
        log.info("Keygen WebClient initialized with baseUrl: {}", baseUrl)
        log.trace("apiKey: {}", cryptoWalletGeneratorProps.awsLambdaApiKey)
    }

    override fun generate(currency: String): CryptoKeyDTO {
        return generate(currency = currency, count = 1)[0]
    }

    override fun generate(currency: String, count: Int): List<CryptoKeyDTO> = runBlocking{
        val fullTasksCount = count / TASK_CAPACITY
        val tasks = concatenate((1..fullTasksCount).map { TASK_CAPACITY }, listOf(count % TASK_CAPACITY))
            .map { KeygenRequest(blockchain = currency, quantity = it) }
        return@runBlocking tasks.pmap { generateBatch(it) }.map { it.data!! }.flatten()
    }

    private suspend fun generateBatch(keygenRequest: KeygenRequest): KeygenDTO {
        log.debug("Starting... URL: {}, keygenRequest: {}", getBaseUrl(), keygenRequest)
        val response = webClient.post()
            .header(X_API_KEY, getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(keygenRequest))
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<KeygenDTO>()
            .awaitSingle()
        val keygenResponse = response.body!!
        log.debug("Finished successfully! URL: {}, Response: {}", getBaseUrl(), keygenResponse)
        return keygenResponse
    }

    private fun handleError(response: ClientResponse) = Mono.error<Throwable>(RuntimeException(response.toString()))

    private fun getBaseUrl() = cryptoWalletGeneratorProps.awsLambdaUrl

    private fun getApiKey() = cryptoWalletGeneratorProps.awsLambdaApiKey

    private data class KeygenRequest(val blockchain: String, val quantity: Int)
    private data class Metadata(val blockchain: String? = null, val quantity: Long? = null)
    private data class KeygenDTO(val data: List<CryptoKeyDTO>? = null, val metadata: Metadata? = null)

}
