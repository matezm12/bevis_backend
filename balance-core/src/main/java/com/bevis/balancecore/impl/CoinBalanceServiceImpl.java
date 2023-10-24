package com.bevis.balancecore.impl;

import com.bevis.balancecore.repository.CoinBalanceRepository;
import com.bevis.balancecore.CoinBalanceService;
import com.bevis.balancecore.domain.CoinBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//fixme noRollbackFor remove from here
@Transactional(noRollbackFor = Exception.class)
@Slf4j
@Service
@RequiredArgsConstructor
class CoinBalanceServiceImpl implements CoinBalanceService {

    private final CoinBalanceRepository coinBalanceRepository;

    @Override
    public Optional<CoinBalance> findCoinByAddress(String address) {
        return coinBalanceRepository.findById(address);
    }

    @Override
    public CoinBalance save(CoinBalance coinBalance) {
        return coinBalanceRepository.save(coinBalance);
    }

    @Override
    public List<CoinBalance> saveAll(Iterable<CoinBalance> coins) {
        return coinBalanceRepository.saveAll(coins);
    }

    @Override
    public List<CoinBalance> findAllCoinsByCurrency(String currency) {
        return coinBalanceRepository.findAllByCurrency(currency);
    }
}
