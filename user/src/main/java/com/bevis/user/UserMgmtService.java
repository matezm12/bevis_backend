package com.bevis.user;

import com.bevis.user.domain.User;
import com.bevis.user.dto.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserMgmtService {
    Optional<User> findById(Long id);

    @Transactional(readOnly = true)
    Page<User> searchAll(UserFilter filter, Pageable pageable);

    User createUser(User user);

    User updateUser(User user);

    User activateUser(Long userId, boolean activated);

    List<String> getAuthorities();
}
