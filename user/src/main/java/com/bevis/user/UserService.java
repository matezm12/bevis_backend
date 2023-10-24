package com.bevis.user;

import com.bevis.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User getCurrentUser();

    Optional<User> findById(Long id);

    User getUserByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    User activateUser(Long userId, boolean activated);

    User createUser(User user, String password);

    User updateUser(User user);

    List<String> getAuthorities();

    Optional<User> findByAssetId(String assetId);

    Optional<User> findOneByEmail(String email);

    Optional<User> findFirstByCodereadrDeviceId(Long codereadrDeviceId);

    void deleteUser(User currentUser);
}
