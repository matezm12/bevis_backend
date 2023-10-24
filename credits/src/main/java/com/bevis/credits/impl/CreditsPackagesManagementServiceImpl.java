package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsPackage;
import com.bevis.credits.repository.CreditsPackageRepository;
import com.bevis.credits.CreditsPackagesManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
class CreditsPackagesManagementServiceImpl implements CreditsPackagesManagementService {

    private final CreditsPackageRepository creditsPackageRepository;

    @Override
    public Page<CreditsPackage> findAll(Pageable pageable) {
        return creditsPackageRepository.findAll(pageable);
    }
}
