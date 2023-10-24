package com.bevis.balancecore;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class CoinBalanceUpdateScheduler {

    private final CoinBalanceUpdatingService coinBalanceUpdatingService;

    @Scheduled(fixedRateString = "${balance-core.balance-update.refreshTimeIntervalInMilliseconds}", initialDelay = 60000)
    void updateCoinBalancesTrigger() {
        coinBalanceUpdatingService.updateCoinBalances();
    }
}
