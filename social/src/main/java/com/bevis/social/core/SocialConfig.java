package com.bevis.social.core;

import com.bevis.social.providers.apple.AppleConnectionFactory;
import com.bevis.social.providers.apple.AppleProfileLoader;
import com.bevis.social.providers.facebook.FacebookConnectionFactory;
import com.bevis.social.providers.google.GoogleConnectionFactory;
import com.bevis.social.providers.facebook.FacebookProfileLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
class SocialConfig {

    private final SocialProps socialProps;
    private final FacebookProfileLoader facebookProfileLoader;

    @Bean
    FacebookConnectionFactory getFacebookConnectionFactory() {
        log.debug("Configuring FacebookConnectionFactory");
        SocialProps.ConnectionProps googleProps = socialProps.getFacebook();
        return new FacebookConnectionFactory(googleProps.getClientId(), googleProps.getClientSecret(), facebookProfileLoader);
    }

    @Bean
    GoogleConnectionFactory getGoogleConnectionFactory() {
        log.debug("Configuring GoogleConnectionFactory");
        SocialProps.ConnectionProps googleProps = socialProps.getGoogle();
        return new GoogleConnectionFactory(googleProps.getClientId(), googleProps.getClientSecret());
    }

    @Bean
    AppleConnectionFactory getAppleConnectionFactory() {
        log.debug("Configuring AppleConnectionFactory");
        SocialProps.AppleConnectionProps appleProps = socialProps.getApple();
        return new AppleConnectionFactory(appleProps.getClientId(), appleProps.getPrivateKey(), appleProfileLoader());
    }

    @Bean
    AppleProfileLoader appleProfileLoader() {
        SocialProps.AppleConnectionProps appleProps = socialProps.getApple();
        return new AppleProfileLoader(
                appleProps.getKeyId(),
                appleProps.getTeamId(),
                appleProps.getClientId(),
                appleProps.getBundleId(),
                appleProps.getPrivateKey()
        );
    }

}
