package com.bevis.balancecore;

import com.bevis.balancecore.domain.CoinBalanceSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CoinBalanceSourceService {

    List<CoinBalanceSource> findAll();

    List<CoinBalanceSource> findAll(Boolean isLive);

    Page<CoinBalanceSource> findAll(Pageable pageable);

    Page<CoinBalanceSource> searchAll(String search, Pageable pageable);

    Optional<CoinBalanceSource> findById(Long id);

    CoinBalanceSource save(CoinBalanceSource coinBalanceSource);

    void deleteById(Long id);

}
