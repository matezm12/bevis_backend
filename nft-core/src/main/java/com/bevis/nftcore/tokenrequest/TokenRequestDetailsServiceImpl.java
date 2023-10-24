package com.bevis.nftcore.tokenrequest;

import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.nftcore.domain.TokenRequest;
import com.bevis.nftcore.tokenrequest.dto.TokenRequestDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
class TokenRequestDetailsServiceImpl implements TokenRequestDetailsService {

    private final TokenRequestService tokenRequestService;
    private final TokenTransferService tokenTransferService;

    @Override
    public TokenRequestDetailsDTO getDetails(Long tokenRequestId) {
        TokenRequest tokenRequest = tokenRequestService.findById(tokenRequestId)
                .orElseThrow(ObjectNotFoundException::new);
        TokenRequestDetailsDTO tokenRequestDetailsDTO = new TokenRequestDetailsDTO();
        tokenRequestDetailsDTO.setId(tokenRequestId);
        tokenRequestDetailsDTO.setCode(tokenRequest.getCode());
        tokenRequestDetailsDTO.setName(tokenRequest.getName());
        tokenRequestDetailsDTO.setStatus(tokenRequest.getStatus());
        tokenRequestDetailsDTO.setTokenId(tokenRequest.getTokenId());
        tokenRequestDetailsDTO.setAddressesCount(tokenRequest.getAddressesCount());
        tokenRequestDetailsDTO.setAddressesProcessed(tokenTransferService.countProcessedByRequestId(tokenRequestId));
        tokenRequestDetailsDTO.setAddressesFailed(tokenTransferService.countFailedByRequestId(tokenRequestId));
        tokenRequestDetailsDTO.setCreatedAt(tokenRequest.getCreatedAt());
        tokenRequestDetailsDTO.setUpdatedAt(tokenRequest.getUpdatedAt());
        tokenRequestDetailsDTO.setBlockchain(tokenRequest.getBlockchain());
        return tokenRequestDetailsDTO;
    }
}
