package com.bevis.credits;

import com.bevis.credits.domain.CreditsBalance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreditsBalanceManagementService {
    Page<CreditsBalance> findAll(Pageable pageable);
}
