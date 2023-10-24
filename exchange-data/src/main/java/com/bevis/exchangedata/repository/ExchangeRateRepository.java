package com.bevis.exchangedata.repository;

import com.bevis.exchangedata.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>, JpaSpecificationExecutor<ExchangeRate> {
    Optional<ExchangeRate> findByCurrencyCode(String code);
    List<ExchangeRate> findAllByCurrencyCodeIn(Set<String> currencyCodes);
}
