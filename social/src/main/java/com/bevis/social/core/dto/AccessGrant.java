package com.bevis.social.core.dto;


import java.io.Serializable;

public class AccessGrant implements Serializable {
    private final String accessToken;
    private final String scope;
    private final String refreshToken;
    private final Long expireTime;

    public AccessGrant(String accessToken) {
        this(accessToken, (String)null, (String)null, (Long)null);
    }

    public AccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.refreshToken = refreshToken;
        this.expireTime = expiresIn != null ? System.currentTimeMillis() + expiresIn * 1000L : null;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getScope() {
        return this.scope;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }
}
