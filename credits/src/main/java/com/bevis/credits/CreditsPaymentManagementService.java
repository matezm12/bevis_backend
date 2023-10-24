package com.bevis.credits;

import com.bevis.credits.domain.CreditsPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreditsPaymentManagementService {
    Page<CreditsPayment> findAll(Pageable pageable);
}
