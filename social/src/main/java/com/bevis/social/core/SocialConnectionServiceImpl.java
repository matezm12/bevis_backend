package com.bevis.social.core;

import com.bevis.social.core.dto.AccessGrant;
import com.bevis.social.core.dto.SocialUserProfile;
import com.bevis.social.core.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
class SocialConnectionServiceImpl implements SocialConnectionService {

    private final SocialUserProfileMapper userProfileMapper;

    //key - providerId, value - appropriate factory
    private Map<String, OAuth2ConnectionFactory> providerFactories;

    @Autowired
    void initSocialConnectionFactories(List<OAuth2ConnectionFactory> connectionFactories){
        providerFactories = connectionFactories.stream()
                .collect(toMap(OAuth2ConnectionFactory::getProviderId, x -> x));
        log.debug("Social factories loaded");
    }

    @Override
    public SocialUserProfile fetchUserProfile(String providerId, String accessToken, Map<String, String> metadata) {
        try {
            OAuth2ConnectionFactory connectionFactory = getConnectionFactory(providerId);
            AccessGrant accessGrant = new AccessGrant(accessToken);
            Connection connection = connectionFactory.createConnection(accessGrant, metadata);
            UserProfile userProfile = fetchUserProfileFromConnection(connection);
            return userProfileMapper.mapSocialUserProfile(userProfile, connection);
        } catch (Exception e){
            log.error("Error fetching user profile. ProviderId:{}, accessToken:{}, message: {}", providerId, accessToken, e.getMessage());
            throw new SocialConnectionException("Error loading user profile from social connection.", e);
        }
    }

    private OAuth2ConnectionFactory getConnectionFactory(String providerId) {
        if (providerFactories.containsKey(providerId)) {
            return providerFactories.get(providerId);
        }
        throw new RuntimeException("Can not get Social Connection factory!");
    }

    private UserProfile fetchUserProfileFromConnection(Connection connection) {
        return connection.fetchUserProfile();
    }
}
