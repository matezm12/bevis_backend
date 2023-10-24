package com.bevis.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public final class SenderUtil {

    public static <T> T sendGetRequest(String url, Class<T> responseType, Map<String, String> headers) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            log.debug("Loading: {}", url);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            for (Map.Entry<String, String> header : headers.entrySet()){
                httpHeaders.add(header.getKey(), header.getValue());
            }
            HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
            return responseEntity.getBody();
        } catch (HttpClientErrorException exception) {
            return processException(exception);
        }
    }

    private static <T> T processException(HttpClientErrorException exception) throws Exception {
        if (HttpStatus.BAD_REQUEST.equals(exception.getStatusCode())) {
            throw new Exception("BAD_REQUEST", exception);
        }
        if (HttpStatus.NOT_FOUND.equals(exception.getStatusCode())) {
            throw new Exception("NOT_FOUND", exception);
        }
        if (HttpStatus.FORBIDDEN.equals(exception.getStatusCode())) {
            throw new Exception("FORBIDDEN", exception);
        }
        throw new Exception("UNDEFINED", exception);
    }
}
