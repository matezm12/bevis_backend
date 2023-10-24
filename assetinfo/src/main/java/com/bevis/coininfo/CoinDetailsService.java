package com.bevis.coininfo;

import com.bevis.coininfo.dto.CoinDetails;
import com.bevis.coininfo.dto.CoinRequest;

public interface CoinDetailsService {
    CoinDetails getCoinDetails(CoinRequest coin, String fiatCurrency);
}
