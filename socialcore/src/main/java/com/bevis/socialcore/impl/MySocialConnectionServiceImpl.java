package com.bevis.socialcore.impl;

import com.bevis.events.dto.user.UserDataDeleteEvent;
import com.bevis.socialcore.domain.SocialUserConnection;
import com.bevis.user.domain.User;
import com.bevis.socialcore.repository.SocialUserConnectionRepository;
import com.bevis.user.UserRepository;
import com.bevis.user.dto.UserRegister;
import com.bevis.socialcore.MySocialConnectionService;
import com.bevis.socialcore.SocialLogin;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.user.UserRegisterService;
import com.bevis.social.core.ConnectionRepository;
import com.bevis.social.core.SocialConnectionService;
import com.bevis.social.core.dto.SocialUserProfile;
import com.bevis.social.core.UsersConnectionRepository;
import com.bevis.social.core.Connection;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
class MySocialConnectionServiceImpl implements MySocialConnectionService {

    private final SocialUserConnectionRepository socialUserConnectionRepository;
    private final UsersConnectionRepository usersConnectionRepository;
    private final SocialConnectionService socialConnectionService;
    private final UserRepository userRepository;
    private final UserRegisterService userRegisterService;

    @Override
    @Transactional
    public UserDetails signUpOrLoginUserBySocialConnection(SocialLogin socialLogin) {
        String provider = socialLogin.getProvider();
        String accessToken = socialLogin.getAccessToken();
        Map<String, String> metadata = socialLogin.getMetadata();
        SocialUserProfile socialUserProfile = socialConnectionService.fetchUserProfile(provider, accessToken, metadata);
        socialUserProfile.setAltEmail(socialLogin.getEmail());//fix for facebook users.
        Optional<SocialUserConnection> socialUserConnectionOpt = loadSocialUserConnection(socialUserProfile);
        UserDetails result;
        if (socialUserConnectionOpt.isPresent()){
            SocialUserConnection socialUserConnection = socialUserConnectionOpt.get();
            Optional<User> userOpt = userRepository.findById(Long.valueOf(socialUserConnection.getUserId()));
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                result = getUserDetails(user);
            } else {
                log.warn("User has not connection");
                User user = createUserIfNotExist(socialUserProfile);
                result = getUserDetails(user);
            }
        } else { //create connection
            User user = createUserIfNotExist(socialUserProfile);
            createSocialConnection(user.getId().toString(), socialUserProfile.getConnection());
            result = getUserDetails(user);
        }
        return result;

    }

    @EventListener
    @Order(3)
    @Override
    public void handleUserDataDeleteEvent(UserDataDeleteEvent e) {
        log.debug("Deleting Social connection User data... [Order 3.]");
        socialUserConnectionRepository.deleteAllByUserId(String.valueOf(e.getUserId()));
    }

    private Optional<SocialUserConnection> loadSocialUserConnection(SocialUserProfile socialUserProfile) {
        return socialUserConnectionRepository
                .findOneByProviderIdAndProviderUserId(socialUserProfile.getProviderId(), socialUserProfile.getProviderUserId());
    }

    private void createSocialConnection(String userId, Connection connection) {
        List<String> connectedUsers = usersConnectionRepository.findUserIdsWithConnection(connection);
        if (connectedUsers.isEmpty()) {
            ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userId);
            connectionRepository.addConnection(connection);
        }
    }

    private User createUserIfNotExist(SocialUserProfile socialUserProfile) {
        final String email = getSocialUserEmail(socialUserProfile);
        final boolean activatedUser = Objects.equals(email, socialUserProfile.getEmail());
        return userRepository.findOneByEmail(email)
                .map((user) -> {
                    if (!user.isActivated()){
                        user.setActivated(true);
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseGet(() -> {
                    UserRegister userRegister = new UserRegister();
                    userRegister.setEmail(email);
                    userRegister.setFirstName(socialUserProfile.getFirstName());
                    userRegister.setLastName(socialUserProfile.getLastName());
                    log.debug("Start registering new account: {}", email);
                    return userRegisterService.registerUser(userRegister, RandomStringUtils.random(10), activatedUser);
                });
    }

    private org.springframework.security.core.userdetails.User getUserDetails(User user) {
        if (user.isActivated()) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPasswordDigest(),
                    Collections.singletonList(
                            new SimpleGrantedAuthority(user.getAuthority())
                    )
            );
        } else {
            return null; //todo refactor this.
        }
    }

    /**
     * Loading email from Socail user profile or get it from optional field from request
     * Fix for facebook users, who has not email
     * @param socialUserProfile
     * @return email
     */
    private String getSocialUserEmail(SocialUserProfile socialUserProfile) {
        final String realEmail = socialUserProfile.getEmail();
        final String altEmail = socialUserProfile.getAltEmail();
        if (Strings.isNullOrEmpty(realEmail)){
            log.warn("Social user provider {} with ID: {} don't have email.", socialUserProfile.getProviderId(), socialUserProfile.getId());
            if (Objects.nonNull(socialUserProfile.getAltEmail())) {
                log.debug("Social User profile: Adding email from optional field: {}", socialUserProfile.getAltEmail());
                return altEmail;
            } else {
                throw new ObjectNotFoundException("Email not found");
            }
        }
        return realEmail;
    }
}
