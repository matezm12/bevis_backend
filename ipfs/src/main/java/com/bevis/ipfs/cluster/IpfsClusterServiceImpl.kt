package com.bevis.ipfs.cluster

import com.bevis.ipfs.IpfsProps
import com.bevis.ipfs.cluster.dto.ErrorResponse
import com.bevis.ipfs.dto.IpfsFile
import com.bevis.ipfs.dto.IpfsPin
import com.bevis.ipfs.exception.IpfsException
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Mono
import java.io.File
import kotlin.properties.Delegates

private const val API_MIME_TYPE = "application/json"
private const val AUTHORIZATION = "Authorization"
private const val AUTH_TYPE = "Basic"

@Service
private class IpfsClusterServiceImpl(
    val ipfsProps: IpfsProps
) : IpfsClusterService {

    private val log = LoggerFactory.getLogger(javaClass)
    private var webClient: WebClient by Delegates.notNull()

    init {
        val baseUrl = getBaseUrl()
        this.webClient = WebClient.builder()
            .baseUrl(getBaseUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, API_MIME_TYPE)
            .defaultHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT)
            .build()
        log.info("WebClient initialized with baseUrl: {}", baseUrl)
        log.trace("apiKey: {}", getApiKey())
    }

    override suspend fun addFile(file: File): IpfsFile {
        val uri = "/add"
        log.debug("Processing: ${getBaseUrl()}$uri")

        val multipartBodyBuilder = MultipartBodyBuilder()
//        builder.part("file", multipartFile.getResource());
        multipartBodyBuilder.part("file", FileSystemResource(file))

        val response = webClient.post().uri(uri)
            .header(AUTHORIZATION, "$AUTH_TYPE ${getApiKey()}")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<FileAddResponse>()
            .awaitSingle()
        log.debug("File added.")

        val responseBody = response.body!!
        return IpfsFile(
            name = responseBody.name,
            hash = responseBody.cid!!["/"],
            size = responseBody.size
        )
    }

    override suspend fun pinFile(ipfsHash: String): IpfsPin {
        val uri = "/pins/$ipfsHash"
        log.debug("Processing: $uri")

        val response = webClient.post().uri(uri)
            .header(AUTHORIZATION, "$AUTH_TYPE ${getApiKey()}")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError) { response -> handleError(response) }
            .toEntity<FileAddResponse>()
            .awaitSingle()
        log.debug("File added.")

        val responseBody = response.body!!
        return IpfsPin(
            hash = responseBody.cid!!["/"],
        )
    }

    private fun handleError(response: ClientResponse) =
        response.bodyToMono(ErrorResponse::class.java)
            .flatMap { t -> Mono.error<Throwable>(IpfsException(t.message)) }

    private fun getBaseUrl() = this.getClusterProps().baseUrl!!

    private fun getApiKey() = this.getClusterProps().apiKey!!

    private fun getClusterProps() = ipfsProps.cluster

}

private data class FileAddResponse(
    var name: String? = null,
    var cid: Map<String, String>? = null,
    var size: String? = null
)
