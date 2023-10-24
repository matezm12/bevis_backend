package com.bevis.nftcore.repository;

import com.bevis.nftcore.domain.TokenRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TokenRequestRepository extends JpaRepository<TokenRequest, Long>, JpaSpecificationExecutor<TokenRequest> {
}
