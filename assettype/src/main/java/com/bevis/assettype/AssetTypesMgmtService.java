package com.bevis.assettype;

import com.bevis.assettype.domain.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AssetTypesMgmtService {
    Optional<AssetType> findById(Long id);

    List<AssetType> findAll();

    Page<AssetType> findAll(Pageable pageable);

    Page<AssetType> searchAll(String search, Pageable pageable);

    AssetType save(AssetType assetType);

    void deleteById(Long id);
}
