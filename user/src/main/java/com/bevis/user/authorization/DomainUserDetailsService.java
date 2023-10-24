package com.bevis.user.authorization;

import com.bevis.user.domain.User;
import com.bevis.user.UserRepository;
import com.bevis.security.BevisUser;
import com.bevis.user.authorization.exception.UserNotActivatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service("userDetailsService")
@Transactional
@RequiredArgsConstructor
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        if (!user.isActivated() || Objects.nonNull(user.getActivationKey())) {
            throw new UserNotActivatedException("User with email " + email + " not activated");
        }
        return new BevisUser(
                user.getEmail(),
                user.getPasswordDigest(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(user.getAuthority())
                )
        ).userAssetId(user.getUserAssetId()).groupAssetId(user.getGroupAssetId());
    }
}
