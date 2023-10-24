package com.bevis.account.impl;

import com.bevis.events.dto.user.UserDataDeleteEvent;
import com.bevis.master.PermissionDeniedException;
import com.bevis.security.util.SecurityUtils;
import com.bevis.user.domain.User;
import com.bevis.user.UserRepository;
import com.bevis.account.AccountService;
import com.bevis.account.dto.Account;
import com.bevis.user.dto.UserRegister;
import com.bevis.account.exception.EmailNotFoundException;
import com.bevis.account.exception.PasswordNotMatchException;
import com.bevis.account.exception.UserNotActivatedException;
import com.bevis.emailcore.EmailService;
import com.bevis.emailcore.dto.PasswordChangedEmail;
import com.bevis.emailcore.dto.ResetPasswordEmail;
import com.bevis.common.exception.DuplicateObjectException;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.user.UserRegisterService;
import com.bevis.user.UserService;
import com.bevis.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRegisterService userRegisterService;
    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public User registerAccount(UserRegister userRegister, String password) {
        String email = userRegister.getEmail();
        log.debug("Start registering new account: {}", email);
        userRepository.findOneByEmail(email).ifPresent( user -> {
            if (!user.isActivated()){
                throw new UserNotActivatedException("User with email " + email + " not activated" );
            }
            throw new DuplicateObjectException("User with email " + email + " already exists" );
        });
        User user = userRegisterService.registerUser(userRegister, password, false);
        log.debug("New user {} successfully saved.", email);
        log.info("account with email {} registered successfully.", email);
        return user;
    }

    @Override
    public void activateAccount(String key) {
        log.debug("Activating user for activation key {}", key);
        User user = userRepository.findOneByActivationKey(key)
                .orElseThrow(() -> new ObjectNotFoundException("User with activation key " + key + " not found"));
        log.debug("User with activation key {} found.", key);
        user.setActivated(true);
        user.setActivationKey(null);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Account getAccount() {
        log.debug("Loading current account...");
        Account account = Account.builder()
                .user(getCurrentUser())
                .build();
        log.debug("Fetched account : {}", account);
        return account;
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        User user = getCurrentUser();
        if (passwordEncoder.matches(currentPassword, user.getPasswordDigest())) {
            user.setPasswordDigest(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            log.debug("User updated after changing email");
            emailService.sendPasswordChangedEmail(PasswordChangedEmail.builder()
                    .receiverEmail(user.getEmail())
                    .user(user)
                    .build());
        } else {
            log.error("Current password {} don't correct for user {}!", currentPassword, user.getEmail());
            throw new PasswordNotMatchException("Current password not correct.");
        }
    }

    @Override
    public void requestPasswordReset(String email) {
        log.debug("Finding user with email {}", email);
        User updatedUser = userRepository.findOneByEmailIgnoreCase(email)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    return user;
                })
                .orElseThrow(() -> new EmailNotFoundException("User with email " + email + " not found"));
        log.info("User with email {} found. Reset key generated", email);
        emailService.sendResetPasswordEmail(ResetPasswordEmail.builder()
                .receiverEmail(updatedUser.getEmail())
                .user(updatedUser)
                .build());
    }

    @Override
    public void finishPasswordReset(String key, String newPassword) {
        log.debug("Reset user password for reset key {}", key);
        User updatedUser = userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
                .map(user -> {
                    user.setPasswordDigest(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    user.setActivated(true);
                    user.setActivationKey(null);
                    return user;
                })
                .orElseThrow(() -> new ObjectNotFoundException("No user was found for this reset key"));
        log.info("Password changed for user.");
        emailService.sendPasswordChangedEmail(PasswordChangedEmail.builder()
                .receiverEmail(updatedUser.getEmail())
                .user(updatedUser)
                .build());
    }

    @Override
    public void deleteAccount(String password) throws PermissionDeniedException {
        User currentUser = getCurrentUser();
        if (!passwordEncoder.matches(password, currentUser.getPasswordDigest())) {
            throw new PasswordNotMatchException("Current password not correct.");
        }
        if (SecurityUtils.isCurrentUserAdmin()) {
            throw new PermissionDeniedException("Admin cannot remove it-self");
        }
        // password is matching. Can remove account
        String email = currentUser.getEmail();

        log.debug("Removing user: {}", email);

        // 1. Delete credits balance stuff...
        // 2. Delete lister assets
        // 3. Delete Social user connection
        // Raising an event, which will be handled in external modules...
        applicationEventPublisher.publishEvent(UserDataDeleteEvent.builder()
                .userId(currentUser.getId())
                .build()
        );

        // Delete user entity from db
        userService.deleteUser(currentUser);

        // Sending email "Account is delete"
        emailService.sendUserRemovedEmail(email);
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }
}
