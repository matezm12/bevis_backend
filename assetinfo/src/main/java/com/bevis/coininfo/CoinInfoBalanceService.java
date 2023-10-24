package com.bevis.coininfo;

import com.bevis.coininfo.dto.CoinInfo;

public interface CoinInfoBalanceService {
    CoinInfo getCoinInfo(String publicKey, String coinCurrency, String currency);
}
