package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsCharge;
import com.bevis.credits.repository.CreditsChargeRepository;
import com.bevis.credits.CreditsChargeManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
class CreditsChargeManagementServiceImpl implements CreditsChargeManagementService {

    private final CreditsChargeRepository creditsChargeRepository;


    @Override
    public Page<CreditsCharge> findAll(Pageable pageable) {
        return creditsChargeRepository.findAll(pageable);
    }
}
