package com.bevis.blockchain.gateway;

import com.bevis.blockchain.BlockchainNodeProps;
import com.bevis.blockchain.gateway.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bevis.blockchain.gateway.JsonParser.parseJsonString;

@Service
@RequiredArgsConstructor
@Slf4j
final class BlockchainNodeGatewayImpl implements BlockchainNodeGateway {

    private final BlockchainNodeProps blockchainNodeProps;
    private final RestTemplate restTemplate;

    @Override
    public BalanceResponse getBalance(String blockchain) {
        final String blockchainCode = blockchain.toUpperCase();
        final String route = "/balance?blockchain={blockchain}";
        final String url = getBaseUrl() + route;
        log.debug("Request: {} for blockchain: {}", url, blockchainCode);
        Map<String, String> uriVariables = Collections.singletonMap("blockchain", blockchainCode);
        HttpEntity<Object> request = new HttpEntity<>(getDefaultHeaders());
        try {
            ResponseEntity<BalanceResponse> result = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    BalanceResponse.class,
                    uriVariables
            );
            if (result.getStatusCode().is2xxSuccessful()) {
                return result.getBody();
            } else {
                throw new Exception(result.toString());
            }
        } catch (HttpClientErrorException e) {
            throw new GatewayException(parseJsonString(e.getResponseBodyAsString(), ErrorResponse.class)
                    .map(ErrorResponse::getError).orElse("Unexpected error"), e);
        } catch (Exception e) {
            throw new GatewayException(e.getMessage(), e);
        }
    }

    @Override
    public TxResponse processTx(String blockchain, String address, String description) {
        final String blockchainCode = blockchain.toUpperCase();
        final String route = "/transactions";
        final String url = getBaseUrl() + route;
        log.debug("Request: {} for blockchain: {}, address: {}, description: {}", url, blockchainCode, address, description);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("blockchain", blockchainCode);
        requestBody.put("address", address);
        requestBody.put("opReturn", description);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, getDefaultHeaders());
        try {
            ResponseEntity<TxResponse> result = restTemplate.postForEntity(url, entity, TxResponse.class);
            if (result.getStatusCode().is2xxSuccessful()) {
                return result.getBody();
            } else {
                throw new Exception(result.toString());
            }

        } catch (HttpClientErrorException e) {
            throw new GatewayException(parseJsonString(e.getResponseBodyAsString(), ErrorResponse.class)
                    .map(ErrorResponse::getError).orElse(e.getResponseBodyAsString()), e);
        } catch (Exception e) {
            throw new GatewayException(e.getMessage(), e);
        }
    }

    @Override
    public TxListResponse getTxList(String blockchain, String address) {
        final String blockchainCode = blockchain.toUpperCase();
        final String route = "/transactions?blockchain={blockchain}&address={address}";
        final String url = getBaseUrl() + route;
        log.debug("Request: {} for blockchain: {}, address: {}", url, blockchainCode, address);
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("blockchain", blockchainCode);
        uriVariables.put("address", address);
        HttpEntity<Object> request = new HttpEntity<>(getDefaultHeaders());
        try {
            ResponseEntity<TxListResponse> result = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    TxListResponse.class,
                    uriVariables
            );
            if (result.getStatusCode().is2xxSuccessful()) {
                return result.getBody();
            } else {
                throw new Exception(result.toString());
            }
        } catch (HttpClientErrorException e) {
            throw new GatewayException(parseJsonString(e.getResponseBodyAsString(), ErrorResponse.class)
                    .map(ErrorResponse::getError).orElse("Unexpected error"), e);
        } catch (Exception e) {
            throw new GatewayException(e.getMessage(), e);
        }
    }

    @Override
    public TransactionResponse getTransactionStatus(String blockchain, String transactionId) {
        final String blockchainCode = blockchain.toUpperCase();
        final String route = "/transaction?blockchain={blockchain}&tx={tx}";
        final String url = getBaseUrl() + route;
        log.debug("Request: {} for blockchain: {}, tx: {}", url, blockchainCode, transactionId);
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("blockchain", blockchainCode);
        uriVariables.put("tx", transactionId);
        HttpEntity<Object> request = new HttpEntity<>(getDefaultHeaders());
        try {
            ResponseEntity<TransactionResponse> result = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    TransactionResponse.class,
                    uriVariables
            );
            if (result.getStatusCode().is2xxSuccessful()) {
                return result.getBody();
            } else {
                throw new Exception(result.toString());
            }
        } catch (HttpClientErrorException e) {
            throw new GatewayException(parseJsonString(e.getResponseBodyAsString(), ErrorResponse.class)
                    .map(ErrorResponse::getError).orElse("Unexpected error"), e);
        } catch (Exception e) {
            throw new GatewayException(e.getMessage(), e);
        }
    }

    private String getBaseUrl() {
        return blockchainNodeProps.getGatewayBaseUrl();
    }

    private HttpHeaders getDefaultHeaders() {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("api_key", blockchainNodeProps.getGatewayApiKey());
        return headers;
    }
}
