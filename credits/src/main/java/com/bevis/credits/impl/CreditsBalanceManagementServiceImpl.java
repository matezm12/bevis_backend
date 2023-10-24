package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.credits.repository.CreditsBalanceRepository;
import com.bevis.credits.CreditsBalanceManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditsBalanceManagementServiceImpl implements CreditsBalanceManagementService {

    private final CreditsBalanceRepository creditsBalanceRepository;

    @Override
    public Page<CreditsBalance> findAll(Pageable pageable) {
        return creditsBalanceRepository.findAll(pageable);
    }
}
