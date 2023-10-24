package com.bevis.balancecore;

import com.bevis.balancecore.dto.CoinBalanceItem;
import com.bevis.balancecore.dto.CoinStatisticItem;

import java.util.List;

public interface CoinBalanceStatisticsService {
    List<CoinStatisticItem> loadAllStatistic();
    List<CoinBalanceItem> loadBalances(String currency);
}
