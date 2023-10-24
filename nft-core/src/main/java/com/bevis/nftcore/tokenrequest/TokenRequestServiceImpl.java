package com.bevis.nftcore.tokenrequest;

import com.bevis.nftcore.domain.TokenRequest;
import com.bevis.nftcore.repository.TokenRequestRepository;
import com.bevis.nftcore.tokenrequest.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bevis.nftcore.tokenrequest.specification.TokenRequestSpecification.bySearchRequest;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
class TokenRequestServiceImpl implements TokenRequestService {

    private final TokenRequestRepository tokenRequestRepository;

    @Override
    public Page<TokenRequest> findAll(SearchDTO search, Pageable pageable) {
        return tokenRequestRepository.findAll(bySearchRequest(search), pageable);
    }

    @Override
    public Optional<TokenRequest> findById(Long tokenRequestId) {
        return tokenRequestRepository.findById(tokenRequestId);
    }

    @Override
    public TokenRequest createOrUpdate(TokenRequest tokenRequest) {
        return tokenRequestRepository.saveAndFlush(tokenRequest);
    }
}
