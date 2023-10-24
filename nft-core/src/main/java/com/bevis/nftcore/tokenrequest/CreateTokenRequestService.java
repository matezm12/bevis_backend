package com.bevis.nftcore.tokenrequest;

import com.bevis.nftcore.tokenrequest.dto.CreateTokenRequestDTO;
import com.bevis.nftcore.tokenrequest.dto.TokenRequestDetailsDTO;

public interface CreateTokenRequestService {
    TokenRequestDetailsDTO createTokenRequest(CreateTokenRequestDTO request);
    TokenRequestDetailsDTO retryFailedTransfers(Long tokenRequestId);
}
