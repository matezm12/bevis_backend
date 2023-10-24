package com.bevis.balancecore.repository;

import com.bevis.balancecore.domain.CoinBalanceSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CoinBalanceSourceRepository extends JpaRepository<CoinBalanceSource, Long>, JpaSpecificationExecutor<CoinBalanceSource> {

    List<CoinBalanceSource> findAllByLive(Boolean isLive);

    Optional<CoinBalanceSource> findByCurrencyAndSourceKey(String currency, String source);

    Optional<CoinBalanceSource> findByCurrencyAndLive(String currency, boolean live);
}
