package com.bevis.nft;

import com.bevis.nft.dto.Balance;
import com.bevis.nft.slp.dto.SendTokensRequest;
import com.bevis.nft.slp.dto.SendTokensResponse;

public interface BitcoinCashSlpService {
    Balance getBalance();
    SendTokensResponse sendTokens(SendTokensRequest request);
}
