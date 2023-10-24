package com.bevis.asset.repository;

import com.bevis.master.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, String> {
}
