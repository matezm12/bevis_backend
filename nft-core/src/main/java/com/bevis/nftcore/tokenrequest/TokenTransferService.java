package com.bevis.nftcore.tokenrequest;

import com.bevis.nftcore.domain.TokenTransfer;
import com.bevis.nftcore.tokenrequest.dto.TokenTransferDTO;
import com.bevis.nftcore.tokenrequest.dto.TokenTransferFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TokenTransferService {

    Page<TokenTransferDTO> findAll(TokenTransferFilterDTO filter, Pageable pageable);

    Integer countProcessedByRequestId(Long tokenRequestId);

    Integer countFailedByRequestId(Long tokenRequestId);

    List<TokenTransfer> saveAll(Iterable<TokenTransfer> tokenTransfers);

    List<TokenTransfer> findAllByRequestId(Long id);

    List<TokenTransfer> findAllFailedByRequestId(Long id);
}
