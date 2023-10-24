package com.bevis.social.providers.google;

import com.bevis.social.core.Connection;
import com.bevis.social.core.OAuth2ConnectionFactory;
import com.bevis.social.core.dto.AccessGrant;
import com.bevis.social.core.dto.ConnectionKey;

import java.util.Map;

public class GoogleConnectionFactory extends OAuth2ConnectionFactory {

    public static final String GOOGLE = "google";

    public GoogleConnectionFactory(String appId, String appSecret) {
        super(appId, appSecret, GOOGLE);
    }

    @Override
    public Connection createConnection(AccessGrant accessGrant, Map<String, String> metadata) {
        ConnectionKey connectionKey = new ConnectionKey(GOOGLE, null);
        return new GoogleConnection(accessGrant, connectionKey, getAppId(), getAppSecret());
    }
}
