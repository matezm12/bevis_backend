package com.bevis.nftcore.tokenrequest;

import com.bevis.nftcore.tokenrequest.dto.TokenRequestDetailsDTO;

public interface TokenRequestDetailsService {
    TokenRequestDetailsDTO getDetails(Long tokenRequestId);
}
