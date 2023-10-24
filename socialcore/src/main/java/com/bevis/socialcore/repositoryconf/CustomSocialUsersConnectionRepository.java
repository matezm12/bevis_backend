package com.bevis.socialcore.repositoryconf;

import com.bevis.socialcore.domain.SocialUserConnection;
import com.bevis.socialcore.repository.SocialUserConnectionRepository;
import com.bevis.social.core.ConnectionRepository;
import com.bevis.social.core.UsersConnectionRepository;
import com.bevis.social.core.Connection;
import com.bevis.social.core.dto.ConnectionKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomSocialUsersConnectionRepository implements UsersConnectionRepository {

    private final SocialUserConnectionRepository socialUserConnectionRepository;

    public CustomSocialUsersConnectionRepository(SocialUserConnectionRepository socialUserConnectionRepository) {
        this.socialUserConnectionRepository = socialUserConnectionRepository;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection connection) {
        ConnectionKey key = connection.getKey();
        List<SocialUserConnection> socialUserConnections =
            socialUserConnectionRepository.findAllByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
        return socialUserConnections.stream()
            .map(SocialUserConnection::getUserId)
            .collect(Collectors.toList());
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new CustomSocialConnectionRepository(userId, socialUserConnectionRepository);
    }

}
