package com.bevis.coininfo;

import com.bevis.coininfo.dto.CoinActivationRequest;

public interface CoinActivationService {
    void activateCoin(CoinActivationRequest coinActivationRequest);

    void deactivateCoin(CoinActivationRequest coinActivationRequest);

}
