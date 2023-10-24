package com.bevis.keygen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Deprecated
//@Service
@Slf4j
class AwsLambdaCryptoKeyGeneratorServiceImpl implements CryptoKeyGeneratorService {

    private static final String API_KEY_HEADER = "x-api-key";
    private static final int THREADS_COUNT = 1000;
    private static final Integer TASK_CAPACITY = 200;

    private final CryptoWalletGeneratorProps cryptoWalletGeneratorProps;
    private final RestTemplateBuilder restTemplateBuilder;

    AwsLambdaCryptoKeyGeneratorServiceImpl(CryptoWalletGeneratorProps cryptoWalletGeneratorProps, RestTemplateBuilder restTemplateBuilder) {
        this.cryptoWalletGeneratorProps = cryptoWalletGeneratorProps;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public List<CryptoKeyDTO> generate(String currency, Integer count) {

        long oldTime = System.currentTimeMillis();
        List<CryptoKeyDTO> result = new ArrayList<>();
        List<Future<KeygenDTO>> futureResults = new ArrayList<>();
        try {
            int tasksCapacity = count / TASK_CAPACITY + 1;
            int threadsCount = Math.min(tasksCapacity, THREADS_COUNT);

            ThreadPoolExecutor executor =
                    (ThreadPoolExecutor) Executors.newFixedThreadPool(threadsCount);
            List<KeygenRequest> tasks = brakePerTasks(currency, count);

            for (int i  = 0; i < tasks.size(); ++i) {
                KeygenRequest task = tasks.get(i);
                int finalI = i;
                futureResults.add(executor.submit(() -> {
                    log.debug("Starting task: {}, keys count: {}", finalI, task.getQuantity());
                    String url = cryptoWalletGeneratorProps.getAwsLambdaUrl();
                    String apiKey = cryptoWalletGeneratorProps.getAwsLambdaApiKey();
                    KeygenRequest keygenRequest = new KeygenRequest();
                    keygenRequest.setBlockchain(task.getBlockchain());
                    keygenRequest.setQuantity(task.getQuantity());
                    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                    headers.add(API_KEY_HEADER, apiKey);
                    HttpEntity<KeygenRequest> requestHttpEntity = new HttpEntity<>(keygenRequest, headers);
                    try {
                        RestTemplate myRestTemplate = restTemplateBuilder.build();
                        ResponseEntity<KeygenDTO> exchangeResponse = myRestTemplate.exchange(url, HttpMethod.POST, requestHttpEntity, KeygenDTO.class);
                        log.debug("Finished task: {}, keys count: {}", finalI, task.getQuantity());
                        return exchangeResponse.getBody();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }));
            }

            for (Future<KeygenDTO> future : futureResults) {
                KeygenDTO keygenDTO = future.get();
                result.addAll(keygenDTO.getData());
                log.warn("Res: " + keygenDTO);
            }
        } catch (Exception e) {
            log.error("Error generating key-pairs. {}", e.getMessage());
            throw new KeygenException("Error generating key-pairs. " + e.getMessage());
        }

        long newTime = System.currentTimeMillis();
        log.debug("Finished: {}, time ms: {}", result.size(), newTime - oldTime);

        return result;
    }

    @Override
    public CryptoKeyDTO generate(String currency) {
        long oldTime = System.currentTimeMillis();
        String url = cryptoWalletGeneratorProps.getAwsLambdaUrl();
        String apiKey = cryptoWalletGeneratorProps.getAwsLambdaApiKey();
        KeygenRequest keygenRequest = new KeygenRequest();
        keygenRequest.setBlockchain(currency);
        keygenRequest.setQuantity(1L);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(API_KEY_HEADER, apiKey);
        HttpEntity<KeygenRequest> requestHttpEntity = new HttpEntity<>(keygenRequest, headers);
        RestTemplate myRestTemplate = restTemplateBuilder.build();
        ResponseEntity<KeygenDTO> exchangeResponse = myRestTemplate.exchange(url, HttpMethod.POST, requestHttpEntity, KeygenDTO.class);
        long newTime = System.currentTimeMillis();
        CryptoKeyDTO cryptoKeyDTO = Objects.requireNonNull(exchangeResponse.getBody()).getData().get(0);
        log.debug("KeyGen Finished: {}, time ms: {}", cryptoKeyDTO, newTime - oldTime);
        log.debug("KeyGen Finished: {}", cryptoKeyDTO);
        return cryptoKeyDTO;
    }

    private List<KeygenRequest> brakePerTasks(String currency, Integer count) {
        Integer remained = count;
        final Integer taskCapacity = TASK_CAPACITY;
        List<KeygenRequest> keygenRequests = new ArrayList<>();
        while (remained > 0) {
            if (remained < taskCapacity) {
                KeygenRequest keygenRequest = new KeygenRequest();
                keygenRequest.setQuantity(remained.longValue());
                keygenRequest.setBlockchain(currency);
                keygenRequests.add(keygenRequest);
                remained = 0;
            } else {
                KeygenRequest keygenRequest = new KeygenRequest();
                keygenRequest.setQuantity(taskCapacity.longValue());
                keygenRequest.setBlockchain(currency);
                keygenRequests.add(keygenRequest);
                remained -= taskCapacity;
            }
        }
        return keygenRequests;
    }
}
