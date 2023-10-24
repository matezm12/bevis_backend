package com.bevis.credits;

import com.bevis.credits.domain.CreditsCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreditsChargeManagementService {
    Page<CreditsCharge> findAll(Pageable pageable);
}
