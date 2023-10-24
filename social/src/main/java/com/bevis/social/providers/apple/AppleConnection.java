package com.bevis.social.providers.apple;

import com.bevis.social.providers.apple.dto.AppleProfile;
import com.bevis.social.core.Connection;
import com.bevis.social.core.dto.AccessGrant;
import com.bevis.social.core.dto.ConnectionKey;
import com.bevis.social.core.dto.UserProfile;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class AppleConnection implements Connection {

    private AccessGrant accessGrant;
    private ConnectionKey connectionKey;
    private AppleProfileLoader appleProfileLoader;
    private Map<String, String> metadata;

    @Override
    public ConnectionKey getKey() {
        return connectionKey;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public UserProfile fetchUserProfile() {
        String device = Optional.ofNullable(metadata)
                .map(x->x.getOrDefault("device", null)).orElse(null);
        AppleProfile appleProfile = appleProfileLoader.loadData(accessGrant.getAccessToken(), device);
        String email = appleProfile.getEmail();
        this.connectionKey = new ConnectionKey(connectionKey.getProviderId(), email);
        return new UserProfile(null, null, null, null, email, null);
    }

}
