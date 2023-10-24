package com.bevis.asset;

import com.bevis.master.domain.Asset;

import java.util.Optional;

public interface AssetService {
    boolean existsById(String assetId);

    Optional<Asset> findById(String assetId);

    void saveAndFlush(Asset asset);

    void deleteById(String assetId);
}
