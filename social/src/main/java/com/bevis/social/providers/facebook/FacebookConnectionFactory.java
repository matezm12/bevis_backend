package com.bevis.social.providers.facebook;

import com.bevis.social.core.Connection;
import com.bevis.social.core.OAuth2ConnectionFactory;
import com.bevis.social.core.dto.AccessGrant;
import com.bevis.social.core.dto.ConnectionKey;

import java.util.Map;

public class FacebookConnectionFactory extends OAuth2ConnectionFactory {
    private final static String FACEBOOK = "facebook";
    private final FacebookProfileLoader facebookProfileLoader;


    public FacebookConnectionFactory(String appId, String appSecret, FacebookProfileLoader facebookProfileLoader) {
        super(appId, appSecret, FACEBOOK);
        this.facebookProfileLoader = facebookProfileLoader;
    }

    @Override
    public Connection createConnection(AccessGrant accessGrant, Map<String, String> metadata) {
        ConnectionKey connectionKey = new ConnectionKey(FACEBOOK, null);
        return new FacebookConnection(accessGrant, connectionKey, getAppId(), getAppSecret(), facebookProfileLoader);
    }
}
