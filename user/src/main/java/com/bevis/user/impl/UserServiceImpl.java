package com.bevis.user.impl;

import com.bevis.common.exception.DuplicateObjectException;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.common.exception.PermissionDeniedException;
import com.bevis.user.domain.User;
import com.bevis.user.UserRepository;
import com.bevis.master.MasterImportService;
import com.bevis.security.AuthoritiesConstants;
import com.bevis.security.util.SecurityUtils;
import com.bevis.user.UserService;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.SUPERADMIN;
import static com.bevis.security.util.SecurityUtils.isCurrentUserInRole;

@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MasterImportService masterImportService;

    @Override
    public User getCurrentUser() {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(ObjectNotFoundException::new);
        log.debug("Fetched user {}", email);
        return getUserByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findOneByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email " + email + " not found!"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    @Transactional
    @Override
    public User activateUser(Long userId, boolean activated) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
        if (Objects.equals(user, getCurrentUser())) {
            throw new PermissionDeniedException("Cannot deactivate myself");
        }
        checkUpdatePermissions(user);
        user.setActivated(activated);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User createUser(User user, String password) {
        userRepository.findOneByEmail(user.getEmail()).ifPresent((existedUser) -> {
            throw new DuplicateObjectException("User already exists");
        });
        updateAuthority(user, user.getAuthority());
        user.setPasswordDigest(passwordEncoder.encode(password));
        user.setUserAssetId(masterImportService.generateMasterOnDefaultBlockchain().getId());
        user.setGroupAssetId(
                Strings.isNullOrEmpty(user.getGroupAssetId()) ? user.getUserAssetId() : user.getGroupAssetId()
        );
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User updatedUser = userRepository.findById(user.getId()).orElse(user);
        checkUpdatePermissions(user);
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        if (!Objects.equals(updatedUser.getAuthority(), user.getAuthority())) {
            updateAuthority(updatedUser, user.getAuthority());
        }
        updatedUser.setGroupAssetId(user.getGroupAssetId());
        return userRepository.save(updatedUser);
    }

    @Override
    public List<String> getAuthorities() {
        return Arrays.asList(
                AuthoritiesConstants.ADMIN,
                AuthoritiesConstants.USER,
                AuthoritiesConstants.VENDOR,
                AuthoritiesConstants.OPERATOR
        );
    }

    @Override
    public Optional<User> findByAssetId(String assetId) {
        return userRepository.findByUserAssetId(assetId);
    }

    @Override
    public Optional<User> findOneByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public Optional<User> findFirstByCodereadrDeviceId(Long codereadrDeviceId) {
        return userRepository.findFirstByCodereadrDeviceId(codereadrDeviceId);
    }

    @Override
    public void deleteUser(User currentUser) {
        log.debug("Deleting user entity...");
        userRepository.delete(currentUser);
    }

    private void checkUpdatePermissions(User user) {
        String authority = user.getAuthority();
        User currentUser = getCurrentUser();
        boolean isAuthoritySuperAdmin = SUPERADMIN.equals(authority);
        boolean isAuthorityAdmin = ADMIN.equals(authority);

        boolean isSameUser = Objects.equals(user.getId(), currentUser.getId());
        boolean isCurrentSuperAdmin = isCurrentUserInRole(SUPERADMIN);
        boolean isCurrentAdmin = isCurrentUserInRole(ADMIN);

        // Allow to update myself
        if (isSameUser) {
            return;
        }
        // Deny updating other users for NON-admin roles
        if (!isCurrentAdmin && !isCurrentSuperAdmin) {
            throw new PermissionDeniedException();
        }
        // Deny to update superadmin
        if (isAuthoritySuperAdmin) {
            throw new PermissionDeniedException();
        }
        // Deny to update admin except by superadmin.
        if (isAuthorityAdmin && !isCurrentSuperAdmin) {
            throw new PermissionDeniedException();
        }
    }

    private void updateAuthority(User updatedUser, String authority) {
        if (SUPERADMIN.equals(authority)) {
            throw new PermissionDeniedException();
        }
        if (SUPERADMIN.equals(updatedUser.getAuthority())) {
            throw new PermissionDeniedException();
        }
        if (ADMIN.equals(authority) && !isCurrentUserInRole(SUPERADMIN)) {
            throw new PermissionDeniedException();
        }
        updatedUser.setAuthority(authority);
    }
}
