package com.bevis.balancecore;

import com.bevis.balance.coinbalance.CryptoBalanceApiConfig;
import com.bevis.balance.coinbalance.dto.BalanceSource;
import com.bevis.balance.coinbalance.dto.DeadSourceEvent;
import com.bevis.balancecore.dto.CoinBalanceSourceUpdateEvent;
import com.bevis.blockchaincore.BlockchainService;
import com.bevis.blockchaincore.dto.BlockchainUpdatedEvent;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.balancecore.domain.CoinBalanceSource;
import com.bevis.events.EventPublishingService;
import com.bevis.events.dto.ErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceApiConfigUpdater {
    private final BlockchainService blockchainService;
    private final CryptoBalanceApiConfig cryptoBalanceApiConfig;
    private final CoinBalanceSourceService coinBalanceSourceService;
    private final EventPublishingService eventPublishingService;

    @Transactional
    @PostConstruct
    void initBalanceSources() {
        coinBalanceSourceService.findAll(true)
                .stream()
                .collect(groupingBy(CoinBalanceSource::getCurrency))
                .forEach((currency, sourcesData) -> {
                    List<BalanceSource> sources = sourcesData.stream()
                            .map(CoinBalanceMapper.INSTANCE::mapBalanceSource)
                            .collect(Collectors.toList());
                    cryptoBalanceApiConfig.setBalanceSources(currency, sources);
                });
        log.info("Balance Sources Configured successfully");
    }

    @Transactional
    @PostConstruct
    void initDefaultBalanceSources() {
        List<Blockchain> blockchains = blockchainService.findAll();
        blockchains.stream().filter(x -> x.getBalanceSource() != null).forEach(blockchain ->
                cryptoBalanceApiConfig.setDefaultCurrencySource(blockchain.getName(), blockchain.getBalanceSource())
        );
        log.info("Balance Default Sources Configured successfully");
    }

    @Async
    @EventListener
    public void handleDeadSourceEvent(DeadSourceEvent e) {
        log.warn("Dead source event received for currency: {}. Message: {}", e.getCurrency(), e.getErrorMessage());
        eventPublishingService.publishEventAsync(
                ErrorEvent.builder()
                        .serviceName("CryptoBalanceApiServiceImpl")
                        .message("Error with Loading balance for currency: " + e.getCurrency() +
                                ", source: " + e.getSource() + ". " +
                                "Detailed reason: " + e.getErrorMessage())
                        .build()
        );
//        coinBalanceSourceService.saveDeadSource(e.getCurrency(), e.getSource());
//        String alternativeSource = coinBalanceSourceService.findFirstLiveSource(e.getCurrency())
//                .map(CoinBalanceSource::getSourceKey)
//                .orElse(null);
//        blockchainService.updateSource(e.getCurrency(), alternativeSource);
    }

    @Async
    @EventListener
    public void handleBlockchainUpdatedEvent(BlockchainUpdatedEvent e) {
        Blockchain blockchain = e.getBlockchain();
        log.warn("Updated blockchain: {}, with source: {}", blockchain.getName(), blockchain.getBalanceSource());
        cryptoBalanceApiConfig.setDefaultCurrencySource(blockchain.getName(), blockchain.getBalanceSource());
    }

    @Async
    @EventListener
    public void handleCoinBalanceSourceUpdateEvent(CoinBalanceSourceUpdateEvent e) {
        CoinBalanceSource coinBalanceSource = e.getCoinBalanceSource();
        if (coinBalanceSource.isLive()) {
            BalanceSource source = CoinBalanceMapper.INSTANCE.mapBalanceSource(coinBalanceSource);
            cryptoBalanceApiConfig.setBalanceSource(coinBalanceSource.getCurrency(), source);
        } else {
            cryptoBalanceApiConfig.removeBalanceSource(coinBalanceSource.getCurrency(), coinBalanceSource.getSourceKey());
        }
    }

}
