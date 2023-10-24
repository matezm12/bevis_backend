package com.bevis.master.repository;

import com.bevis.master.domain.Master;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MasterRepository extends JpaRepository<Master, String>, JpaSpecificationExecutor<Master> {

    Optional<Master> findFirstByPublicKey(String publicKey);

    Optional<Master> findFirstByPublicKeyOrId(String publicKey, String id);

    List<Master> findAllByPublicKeyIn(Iterable<String> publicKeys);

    boolean existsByIdOrPublicKey(String id, String publicKey);

    List<Master> findAllByCodereadrScanId(String scanId);

    List<Master> findAllByIdIn(List<String> assetIDs);

    List<Master> findAllByIdInOrPublicKeyIn(List<String> assetIDs, List<String> publicKeys);
}
