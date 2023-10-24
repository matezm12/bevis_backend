package com.bevis.nftcore.tokenrequest;

import com.bevis.nftcore.domain.TokenRequest;
import com.bevis.nftcore.tokenrequest.dto.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TokenRequestService {
    Page<TokenRequest> findAll(SearchDTO search, Pageable pageable);

    TokenRequest createOrUpdate(TokenRequest tokenRequest);

    Optional<TokenRequest> findById(Long tokenRequestId);
}
