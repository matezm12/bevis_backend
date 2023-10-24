package com.bevis.socialcore.repository;

import com.bevis.socialcore.domain.SocialUserConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialUserConnectionRepository extends JpaRepository<SocialUserConnection, Long> {
    List<SocialUserConnection> findAllByProviderIdAndProviderUserId(String providerId, String providerUserId);

    List<SocialUserConnection> findAllByUserIdAndProviderIdOrderByRankAsc(String userId, String providerId);

    void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    Optional<SocialUserConnection> findOneByProviderIdAndProviderUserId(String providerId, String providerUserId);

    void deleteAllByUserId(String userId);
}
