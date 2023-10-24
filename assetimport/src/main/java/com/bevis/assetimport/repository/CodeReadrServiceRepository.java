package com.bevis.assetimport.repository;

import com.bevis.assetimport.domain.CodeReadrService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CodeReadrServiceRepository extends JpaRepository<CodeReadrService, Long>, JpaSpecificationExecutor<CodeReadrService> {
    Optional<CodeReadrService> findByServiceId(String serviceId);
}
