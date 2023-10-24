package com.bevis.credits.repository;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditsBalanceRepository extends JpaRepository<CreditsBalance, Long> {
    Page<CreditsBalance> findAllByUserOrderByDateTimeDesc(User user, Pageable pageable);

    Optional<CreditsBalance> findFirstByUserOrderByDateTimeDesc(User user);

    void deleteAllByUserId(Long userId);
}
