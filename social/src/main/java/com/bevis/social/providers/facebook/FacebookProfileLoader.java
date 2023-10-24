package com.bevis.social.providers.facebook;

import com.bevis.social.core.SocialConnectionException;
import com.bevis.social.core.dto.UserProfile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Deprecated
@Slf4j
@Service
public class FacebookProfileLoader {

    private final RestTemplate restTemplate;

    FacebookProfileLoader(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public UserProfile getProfile(String id, String accessToken) {
        try {
            String params = "fields=id,name,email,first_name,last_name&access_token=" + accessToken;
            String url = "https://graph.facebook.com/v3.2/" + id + "?" + params;
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root =  new ObjectMapper().readTree(response.getBody());
                return new UserProfile(root.path("id").asText(), root.path("name").asText(), root.path("first_name").asText(),
                        root.path("last_name").asText(), root.path("email").asText(), root.path("id").asText());
            } else {
                log.error("Error loading facebook profile");
                throw new SocialConnectionException("Error loading facebook profile");
            }
        } catch (Exception ex) {
            log.error("Error loading facebook profile");
            throw new SocialConnectionException("Error loading facebook profile", ex);
        }
    }
}
