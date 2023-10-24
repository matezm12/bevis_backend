package com.bevis.credits.repository;

import com.bevis.credits.domain.CreditsCharge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditsChargeRepository extends JpaRepository<CreditsCharge, Long> {
    boolean existsByPurchaseId(String purchaseId);
}
