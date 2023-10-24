package com.bevis.coininfo;

import com.bevis.coininfo.dto.Coin;
import com.bevis.coininfo.dto.CoinRequest;
import com.bevis.coininfo.dto.CryptoCoinsInfo;
import com.bevis.master.domain.Master;
import lombok.NonNull;

import java.util.List;

public interface CoinInfoService {

    CryptoCoinsInfo loadCoinAssetsInfo(List<CoinRequest> coins, String fiatCurrency);

    Coin loadCoin(@NonNull Master master, @NonNull String fiatCurrency, Double manualBalance);
}
