package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsPayment;
import com.bevis.credits.repository.CreditsPaymentRepository;
import com.bevis.credits.CreditsPaymentManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
class CreditsPaymentManagementServiceImpl implements CreditsPaymentManagementService {

    private final CreditsPaymentRepository creditsPaymentRepository;

    @Override
    public Page<CreditsPayment> findAll(Pageable pageable) {
        return creditsPaymentRepository.findAll(pageable);
    }
}
