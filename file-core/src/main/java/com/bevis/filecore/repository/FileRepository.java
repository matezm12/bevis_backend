package com.bevis.filecore.repository;

import com.bevis.filecore.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long>, JpaSpecificationExecutor<File> {
    List<File> findAllByBlockIsNull();

    Optional<File> findFirstByIpfs(String ipfs);

    List<File> findAllByAssetId(String assetId);
}
