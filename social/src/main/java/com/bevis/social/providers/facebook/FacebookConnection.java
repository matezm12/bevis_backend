package com.bevis.social.providers.facebook;

import com.bevis.social.core.Connection;
import com.bevis.social.core.dto.AccessGrant;
import com.bevis.social.core.dto.ConnectionKey;
import com.bevis.social.core.dto.UserProfile;

public class FacebookConnection implements Connection {

    private final FacebookProfileLoader facebookProfileLoader;
    private AccessGrant accessGrant;
    private ConnectionKey connectionKey;
    private String appId;
    private String appSecret;
    private String imageUrl;
    private String displayName;

    public FacebookConnection(AccessGrant accessGrant, ConnectionKey connectionKey, String appId, String appSecret, FacebookProfileLoader facebookProfileLoader) {
        this.accessGrant = accessGrant;
        this.connectionKey = connectionKey;
        this.appId = appId;
        this.appSecret = appSecret;
        this.facebookProfileLoader = facebookProfileLoader;
    }

    @Override
    public ConnectionKey getKey() {
        return connectionKey;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public UserProfile fetchUserProfile() {
        String accessToken = accessGrant.getAccessToken();
        UserProfile me = facebookProfileLoader.getProfile("me", accessToken);
        this.imageUrl = null;
        this.displayName = me.getName();
        this.connectionKey = new ConnectionKey(connectionKey.getProviderId(), me.getId());
        return me;
    }

}
