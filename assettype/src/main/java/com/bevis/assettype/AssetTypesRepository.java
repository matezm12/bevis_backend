package com.bevis.assettype;

import com.bevis.assettype.domain.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

interface AssetTypesRepository extends JpaRepository<AssetType, Long>, JpaSpecificationExecutor<AssetType> {
    Optional<AssetType> findFirstByKey(String key);

    List<AssetType> findAllByDeleted(boolean deleted);

    Page<AssetType> findAllByDeleted(boolean deleted, Pageable pageable);

    List<AssetType> findAllByIsProductTrue();

    Optional<AssetType> findOneByKey(String key);
}
