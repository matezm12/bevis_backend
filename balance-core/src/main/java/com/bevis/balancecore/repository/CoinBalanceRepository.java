package com.bevis.balancecore.repository;

import com.bevis.balancecore.dto.CoinBalanceItem;
import com.bevis.balancecore.dto.CoinStatisticItem;
import com.bevis.balancecore.domain.CoinBalance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinBalanceRepository extends JpaRepository<CoinBalance, String> {

    List<CoinBalance> findAllByCurrency(String currency);

    @Query("SELECT new com.bevis.balancecore.dto.CoinBalanceItem("+
            "cb.currency," +
            "cb.publicKey," +
            "cb.balance/cb.divider," +
            "(er.usdPrice * cb.balance)/cb.divider," +
            "cb.lastScanned," +
            "cb.createdAt," +
            "cb.updatedAt) " +
            "FROM CoinBalance cb " +
            "JOIN ExchangeRate er ON cb.currency=er.currencyCode " +
            "WHERE cb.currency = :currency ")
    List<CoinBalanceItem> loadBalances(@Param("currency") String currency, Pageable pageable);

    @Query("SELECT new com.bevis.balancecore.dto.CoinStatisticItem(" +
            "cb.currency, " +
            "COUNT(0), " +
            "SUM(cb.balance / cb.divider), " +
            "(SUM((er.usdPrice * cb.balance)/cb.divider))" +
            ") FROM CoinBalance cb " +
            "JOIN ExchangeRate er ON cb.currency=er.currencyCode " +
            "GROUP BY cb.currency")
    List<CoinStatisticItem> loadAllStatistic();

    @Query("SELECT new com.bevis.balancecore.dto.CoinStatisticItem(" +
            "COUNT(0), " +
            "SUM((er.usdPrice * cb.balance)/cb.divider)" +
            ") FROM CoinBalance cb " +
            "JOIN ExchangeRate er ON cb.currency=er.currencyCode ")
    CoinStatisticItem loadSummaryStatistic();
}
