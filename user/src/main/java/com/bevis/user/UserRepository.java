package com.bevis.user;

import com.bevis.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByActivationKey(String key);

    Optional<User> findOneByResetKey(String key);

    Optional<User> findByUserAssetId(String assetId);

    Optional<User> findFirstByCodereadrDeviceId(Long codereadrDeviceId);
}
