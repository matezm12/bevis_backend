package com.bevis.nft.slp

import com.bevis.nft.slp.dto.Balance
import com.bevis.nft.slp.dto.ErrorResponse
import com.bevis.nft.slp.dto.SendTokensRequest
import com.bevis.nft.slp.dto.SendTokensResponse
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Mono
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient
import kotlin.properties.Delegates


private const val API_MIME_TYPE = "application/json"
private const val X_API_KEY = "api_key"

internal class ElectronCashSlpApiWebClientImpl(private val electronCashProps: ElectronCashSlpProps) :
    ElectronCashSlpApiWebClient {

    private val log = LoggerFactory.getLogger(javaClass)
    private var webClient: WebClient by Delegates.notNull()

    init {
        val baseUrl = getBaseUrl()

        val httpClient = HttpClient.create()
            .tcpConfiguration { client: TcpClient ->
                client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 29000)
                    .doOnConnected { conn: Connection ->
                        conn
                            .addHandlerLast(ReadTimeoutHandler(29))
                            .addHandlerLast(WriteTimeoutHandler(29))
                    }
            }

        val connector: ClientHttpConnector = ReactorClientHttpConnector(httpClient.wiretap(true))

        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(connector)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, API_MIME_TYPE)
            .defaultHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT)
            .build()
        log.info("ElectronCash-SLP WebClient initialized with baseUrl: $baseUrl")
        log.trace("apiKey: ${getApiKey()}")
    }

    override suspend fun getBalance(): Balance {
        val uri = "/electron-cash-slp/balance"

        val response = webClient.get().uri(uri)
            .header(X_API_KEY, getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<Balance>()
            .awaitSingle()

        val balance = response.body
        log.debug("Finished successfully! Blockchain balance loaded ${getBaseUrl() + uri}:{}, Response: $balance")

        return balance!!
    }

    override suspend fun sendTokens(request: SendTokensRequest): SendTokensResponse {
        val uri = "/electron-cash-slp/send"

        val response = webClient.post().uri(uri)
            .header(X_API_KEY, getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<SendTokensResponse>()
            .awaitSingle()

        val responseBody = response.body
        log.debug("Finished successfully! Tokens sent: ${getBaseUrl() + uri}:{}, Response: $responseBody")

        return responseBody!!
    }

    private fun handleError(response: ClientResponse) =
        response.bodyToMono(ErrorResponse::class.java)
            .flatMap { t -> Mono.error<Throwable>(ElectronCashSlpException(t.message, t.statusCode)) }


    private fun getBaseUrl() = electronCashProps.baseUrl

    private fun getApiKey() = electronCashProps.apiKey
}
