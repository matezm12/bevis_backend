package com.bevis.balancecore.impl;

import com.bevis.balancecore.repository.CoinBalanceRepository;
import com.bevis.balancecore.CoinBalanceStatisticsService;
import com.bevis.balancecore.dto.CoinBalanceItem;
import com.bevis.balancecore.dto.CoinStatisticItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
class CoinBalanceStatisticsServiceImpl implements CoinBalanceStatisticsService {

    private final CoinBalanceRepository coinBalanceRepository;

    @Override
    public List<CoinStatisticItem> loadAllStatistic() {
        return Stream.concat(
                        Stream.of(coinBalanceRepository.loadSummaryStatistic()).peek(x -> x.setCurrency("ALL CURRENCIES")),
                        coinBalanceRepository.loadAllStatistic()
                                .stream()
                                .peek(x-> x.setSumBalance(Objects.nonNull(x.getSumBalance()) ? x.getSumBalance() : 0))
                                .peek(x-> x.setSumUsd(Objects.nonNull(x.getSumUsd()) ? x.getSumUsd() : 0))
                                .sorted(Comparator.comparingDouble(CoinStatisticItem::getSumUsd).reversed()))
                .collect(Collectors.toList());

    }

    @Override
    public List<CoinBalanceItem> loadBalances(String currency) {
        return coinBalanceRepository.loadBalances(currency, PageRequest.of(0, 15, Sort.by(Sort.Order.desc("balance"))));
    }
}
