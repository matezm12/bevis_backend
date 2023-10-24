package com.bevis.social.core;

import com.bevis.social.core.dto.AccessGrant;
import lombok.Data;

import java.util.Map;

@Data
public abstract class OAuth2ConnectionFactory {
    private String appId;
    private String appSecret;
    private String providerId;

    public OAuth2ConnectionFactory(String appId, String appSecret, String providerId) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.providerId = providerId;
    }

    public abstract Connection createConnection(AccessGrant accessGrant, Map<String, String> metadata);
}
