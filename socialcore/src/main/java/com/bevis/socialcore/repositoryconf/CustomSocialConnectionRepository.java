package com.bevis.socialcore.repositoryconf;

import com.bevis.socialcore.domain.SocialUserConnection;
import com.bevis.socialcore.repository.SocialUserConnectionRepository;
import com.bevis.social.core.ConnectionRepository;
import com.bevis.social.core.Connection;
import com.bevis.social.core.dto.ConnectionKey;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

class CustomSocialConnectionRepository implements ConnectionRepository {

    private final String userId;
    private final SocialUserConnectionRepository socialUserConnectionRepository;

    CustomSocialConnectionRepository(String userId, SocialUserConnectionRepository socialUserConnectionRepository) {
        this.userId = userId;
        this.socialUserConnectionRepository = socialUserConnectionRepository;
    }

    @Override
    @Transactional
    public void addConnection(Connection connection) {
        Long rank = getNewMaxRank(connection.getKey().getProviderId()).longValue();
        SocialUserConnection socialUserConnectionToSave = connectionToUserSocialConnection(connection, rank);
        socialUserConnectionRepository.save(socialUserConnectionToSave);
    }

    @Override
    @Transactional
    public void removeConnection(ConnectionKey connectionKey) {
        socialUserConnectionRepository.deleteByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    private Double getNewMaxRank(String providerId) {
        List<SocialUserConnection> socialUserConnections = socialUserConnectionRepository.findAllByUserIdAndProviderIdOrderByRankAsc(userId, providerId);
        return socialUserConnections.stream()
            .mapToDouble(SocialUserConnection::getRank)
            .max()
            .orElse(0D) + 1D;
    }

    private SocialUserConnection connectionToUserSocialConnection(Connection connection, Long rank) {
        return new SocialUserConnection(
                null,
            userId,
            connection.getKey().getProviderId(),
            connection.getKey().getProviderUserId(),
            rank,
            connection.getDisplayName(),
            connection.getImageUrl()
        );
    }

}
