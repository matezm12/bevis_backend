package com.bevis.ipfs.pinata;

import com.bevis.ipfs.pinata.dto.PinResponse;
import com.bevis.ipfs.pinata.exeption.PinataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
class PinataIpfsServiceImpl implements PinataIpfsService {

    private static final String API_URL = "https://api.pinata.cloud/";
    private static final String PIN_FILE_TO_IPFS = "pinning/pinFileToIPFS";
    private static final String PINATA_API_KEY = "pinata_api_key";
    private static final String PINATA_SECRET_API_KEY = "pinata_secret_api_key";

    private final PinataProps pinataProps;

    @Override
    public PinResponse pinFileToIpfs(File file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set(PINATA_API_KEY, pinataProps.getApiKey());
        headers.set(PINATA_SECRET_API_KEY, pinataProps.getSecretApiKey());
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = API_URL + PIN_FILE_TO_IPFS;
        try {

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setBufferRequestBody(false);
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            ResponseEntity<PinResponse> response = restTemplate
                    .postForEntity(serverUrl, requestEntity, PinResponse.class);

            log.debug("Pinata response: {}", response.getBody());
            return response.getBody();
        } catch (Exception e){
            log.error(e.getMessage());
            throw new PinataException("Error pinning file " + file.getName(), e);
        }
    }

}
