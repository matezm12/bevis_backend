package com.bevis.credits.repository;

import com.bevis.credits.domain.CreditsPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditsPaymentRepository extends JpaRepository<CreditsPayment, Long> {
}
