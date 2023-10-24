package com.bevis.credits.repository;

import com.bevis.credits.domain.CreditsPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditsPackageRepository extends JpaRepository<CreditsPackage, Long> {
        Optional<CreditsPackage> findFirstByProductId(String productId);
}
