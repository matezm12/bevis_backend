package com.bevis.lister;

import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ListerAssetRepository extends JpaRepository<ListerAsset, Long>, JpaSpecificationExecutor<ListerAsset> {

    Optional<ListerAsset> findFirstByMasterIdAndUser(String assetId, User user);

    boolean existsByMasterIdAndUser(String assetId, User user);

    Optional<ListerAsset> findOneByMasterIdAndUser(String assetId, User user);

    Page<ListerAsset> findAllByUser(User user, Pageable pageable);

    void deleteByMasterAndUser(Master master, User user);

    void deleteAllByUserId(Long userId);
}
