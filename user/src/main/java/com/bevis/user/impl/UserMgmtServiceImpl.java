package com.bevis.user.impl;

import com.bevis.common.exception.PermissionDeniedException;
import com.bevis.events.EventPublishingService;
import com.bevis.user.domain.User;
import com.bevis.security.util.SecurityUtils;
import com.bevis.user.UserMgmtService;
import com.bevis.user.UserService;
import com.bevis.user.dto.OnUserCreatedEvent;
import com.bevis.user.dto.UserFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.USER;
import static com.bevis.user.impl.UserSpecification.bySearchQuery;

@Service
@Slf4j
@RequiredArgsConstructor
class UserMgmtServiceImpl implements UserMgmtService {

    private final UserService userService;
    private final EventPublishingService eventPublishingService;

    @Override
    public Optional<User> findById(Long id) {
        return userService.findById(id);
    }

    @Override
    public Page<User> searchAll(UserFilter filter, Pageable pageable) {
        if (SecurityUtils.isCurrentUserAdmin()) {
            return userService.findAll(bySearchQuery(filter), pageable);
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public User createUser(User user) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        User createdUser = userService.createUser(user, password);
        eventPublishingService.publishEvent(new OnUserCreatedEvent(user, password));
        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        if (Objects.isNull(user.getId())){
            throw new RuntimeException("Id is null");
        }
        return userService.updateUser(user);
    }

    @Override
    public User activateUser(Long userId, boolean activated) {
        return userService.activateUser(userId, activated);
    }

    @Override
    public List<String> getAuthorities() {
        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        return userService.getAuthorities().stream()
                .filter(x->isAdmin || (!Objects.equals(ADMIN, x) && !Objects.equals(USER, x)))
                .collect(Collectors.toList());
    }

}
