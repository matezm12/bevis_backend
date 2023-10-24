package com.bevis.jsonrpc.impl;

import com.bevis.jsonrpc.JsonRpcService;
import com.bevis.jsonrpc.exception.JsonRpcException;
import com.bevis.jsonrpc.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static com.bevis.common.util.DeserializerUtil.deserializeJsonToObject;
import static com.bevis.common.util.MapUtil.convertObjectToMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonRpcServiceImpl implements JsonRpcService {

    private final RestTemplate restTemplate;

    @Override
    public <T extends JsonRpcResponse> T processJsonRpcCall(String url, JsonRpcCall rpcCall, Class<T> responseType) {
        return processJsonRpcCall(url, new JsonRpcSecuredCall(rpcCall, null), responseType);
    }

    @Override
    public  <T extends JsonRpcResponse> T processJsonRpcCall(String url, JsonRpcSecuredCall securedCall, Class<T> responseType){
        log.debug("Start processing JsonRpc call - url: {}, rpcCall: {}", url, securedCall);
        JsonRpcCall rpcCall = securedCall.getJsonRpcCall();
        Map<String, Object> requestBody = convertObjectToMap(rpcCall);
        try {
            HttpEntity<Map<String, Object>> requestEntity;
            JsonRpcSecurity security = securedCall.getSecurity();
            if (Objects.nonNull(security)) {
                HttpHeaders securityHeaders = createSecurityHeaders(security.getUser(), security.getPassword());
                log.debug("Configured security for call  url: {}, rpcCall: {}", url, rpcCall);
                requestEntity = new HttpEntity<>(requestBody, securityHeaders);
            } else {
                requestEntity = new HttpEntity<>(requestBody);
                log.warn("Processing not secured JsonRpc call! url: {}, rpcCall: {}", url, rpcCall);
            }
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.trace("response processed");
            String responseBody = responseEntity.getBody();
            T jsonRpcResponse = deserializeJsonToObject(responseBody, responseType);
            log.trace("jsonRpcResponse: {}", jsonRpcResponse);
            JsonRpcError error = jsonRpcResponse.getError();
            if (Objects.nonNull(error)){
                log.error("Error processing method {} on server, details: {}", rpcCall.getMethod(), error);
                throw new JsonRpcException("Error processing method " + rpcCall.getMethod() + " on Server");
            }
            log.debug("Finished method, request: {}, response: {}", rpcCall, jsonRpcResponse);
            return jsonRpcResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new JsonRpcException("Error sending JsonRpc call to server, reason: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createSecurityHeaders(String username, String password){
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII) );
        String authHeader = "Basic " + new String( encodedAuth );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set( "Authorization", authHeader );
        return httpHeaders;
    }
}
