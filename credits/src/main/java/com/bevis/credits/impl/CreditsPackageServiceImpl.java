package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsPackage;
import com.bevis.credits.repository.CreditsPackageRepository;
import com.bevis.credits.CreditsPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class CreditsPackageServiceImpl implements CreditsPackageService {
    private final CreditsPackageRepository creditsPackageRepository;

    @Override
    public List<CreditsPackage> getCreditsPackages() {
        return creditsPackageRepository.findAll();
    }
}
