package com.bevis.appbe.config;

import com.bevis.security.BevisUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static com.bevis.security.util.SecurityUtils.tryToGetCurrentUser;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    private static final String ROOT_ASSET = "000000";

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            String userAssetId = tryToGetCurrentUser()
                    .map(BevisUser::getUserAssetId)
                    .orElse(ROOT_ASSET);
            return Optional.of(userAssetId);
        };
    }
}
