package com.bevis.blockchaincore;

import com.bevis.blockchaincore.domain.Blockchain;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BlockchainRepository extends JpaRepository<Blockchain, Long>, JpaSpecificationExecutor<Blockchain> {

    @Cacheable(value = "blockchains", key = "#name")
    Optional<Blockchain> findByNameIgnoreCase(String name);

    List<Blockchain> findAllByExchangeRatesTrue();

    List<Blockchain> findAllByBalanceUpdateTrue();
}
