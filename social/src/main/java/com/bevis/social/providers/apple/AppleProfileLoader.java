package com.bevis.social.providers.apple;

import com.bevis.social.core.CryptoUtil;
import com.bevis.social.core.SocialConnectionException;
import com.bevis.social.providers.apple.dto.AppleProfile;
import com.bevis.social.providers.apple.dto.IdTokenBody;
import com.bevis.social.providers.apple.dto.TokenResponse;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static com.bevis.social.core.CryptoUtil.parseJwtTokenBody;

@Slf4j
@RequiredArgsConstructor
public class AppleProfileLoader {

    public static final String APPLE_AUTH_URL = "https://appleid.apple.com/auth/token";
    private static final int CLIENT_SECRET_EXPIRATION = 1000 * 60 * 5; // 5 min

    private final String keyId;
    private final String teamId;
    private final String clientId;
    private final String bundleId;
    private final String privateKey;

    public AppleProfile loadData(String authorizationCode, String device) throws SocialConnectionException {

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            String realClientId = getRealClientId(device);
            map.add("client_id", realClientId); // app_id like com.app.id
            String clientSecret = generateClientSecretJwt(realClientId);   // generated jwt
            map.add("client_secret", clientSecret);
            map.add("grant_type", "authorization_code");
            map.add("code", authorizationCode);  // JWT code we got from iOS
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            TokenResponse response = restTemplate.postForObject(APPLE_AUTH_URL, request, TokenResponse.class);

            return Optional.ofNullable(response)
                    .map(TokenResponse::getIdToken)
                    .map(idToken -> parseJwtTokenBody(idToken, IdTokenBody.class))
                    .map(IdTokenBody::getEmail)
                    .map(AppleProfile::new)
                    .orElseThrow();
        } catch (Exception e) {
            throw new SocialConnectionException("Error SignIn with Apple:" + e.getMessage(), e);
        }

    }

    private String getRealClientId(String device) {
        if (Objects.equals(device, "ios") || Objects.equals(device, "android")) {
            return bundleId;
        } else {
            return clientId;
        }
    }

    private String generateClientSecretJwt(String realClientId) throws IOException {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, keyId) // key id I got from Apple
                .setIssuer(teamId)
                .setAudience("https://appleid.apple.com")
                .setSubject(realClientId) // app id com.app.id
                .setExpiration(new Date(System.currentTimeMillis() + CLIENT_SECRET_EXPIRATION))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.ES256, CryptoUtil.privateKeyFromPem(privateKey)) // ECDSA using P-256 and SHA-256
                .compact();
    }


}
