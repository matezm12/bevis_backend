package com.bevis.asset.impl;

import com.bevis.asset.AssetService;
import com.bevis.asset.repository.AssetRepository;
import com.bevis.master.domain.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String assetId) {
        return assetRepository.existsById(assetId);
    }

    @Override
    public Optional<Asset> findById(String assetId) {
        return assetRepository.findById(assetId);
    }

    @Override
    public void saveAndFlush(Asset asset) {
        assetRepository.saveAndFlush(asset);
    }

    @Override
    public void deleteById(String assetId) {
        assetRepository.deleteById(assetId);
    }
}
