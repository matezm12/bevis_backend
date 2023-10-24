package com.bevis.balancecore;

import com.bevis.balancecore.domain.CoinBalance;

import java.util.List;
import java.util.Optional;

public interface CoinBalanceService {

    Optional<CoinBalance> findCoinByAddress(String address);

    CoinBalance save(CoinBalance coinBalance);

    List<CoinBalance> saveAll(Iterable<CoinBalance> coins);

    List<CoinBalance> findAllCoinsByCurrency(String currency);
}
