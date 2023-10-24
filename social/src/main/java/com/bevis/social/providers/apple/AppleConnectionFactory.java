package com.bevis.social.providers.apple;

import com.bevis.social.core.Connection;
import com.bevis.social.core.OAuth2ConnectionFactory;
import com.bevis.social.core.dto.AccessGrant;
import com.bevis.social.core.dto.ConnectionKey;

import java.util.Map;

public class AppleConnectionFactory extends OAuth2ConnectionFactory {
    private final static String APPLE = "apple";
    private final AppleProfileLoader appleProfileLoader;

    public AppleConnectionFactory(String clientId, String privateKey, AppleProfileLoader appleProfileLoader) {
        super(clientId, privateKey, APPLE);
        this.appleProfileLoader = appleProfileLoader;
    }

    @Override
    public Connection createConnection(AccessGrant accessGrant, Map<String, String> metadata) {
        ConnectionKey connectionKey = new ConnectionKey(getProviderId(), null);
        return new AppleConnection(accessGrant, connectionKey, appleProfileLoader, metadata);
    }
}
