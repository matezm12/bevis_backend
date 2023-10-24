package com.bevis.nftcore.repository;

import com.bevis.nftcore.domain.TokenTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TokenTransferRepository extends JpaRepository<TokenTransfer, Long>, JpaSpecificationExecutor<TokenTransfer> {

    Integer countByTokenRequestIdAndTransactionIdNotNull(Long tokenRequestId);

    Integer countByTokenRequestIdAndErrorMessageNotNull(Long tokenRequestId);

    List<TokenTransfer> findAllByTokenRequestId(Long requestId);

    List<TokenTransfer> findAllByTokenRequestIdAndErrorMessageNotNull(Long requestId);
}
