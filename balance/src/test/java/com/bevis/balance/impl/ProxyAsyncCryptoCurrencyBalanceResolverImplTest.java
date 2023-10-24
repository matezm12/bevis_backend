package com.bevis.balance.impl;

import com.bevis.balance.BalanceApplication;
import com.bevis.balance.dto.Balance;
import com.bevis.balance.dto.WalletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalanceApplication.class)
@TestPropertySource("classpath:application.yml")
public class ProxyAsyncCryptoCurrencyBalanceResolverImplTest {
    @Autowired
    private ProxyAsyncCryptoCurrencyBalanceResolverImpl proxyAsyncCryptoCurrencyBalanceResolver;

    @Test
    public void testGetBalance() {
        List<WalletRequest> req = Stream.of(
                WalletRequest.builder()
                        .currency("DENARIUS")
                        .address("DGTDLEEQ2gvzMRYnaCAZJpQt39UUBU1xgn")
                        .build(),
                WalletRequest.builder()
                        .currency("CLUB")
                        .address("1KQDe9wA57sEJ2ue52kupuMwRoGjNvjHD6")
                        .build(),
                WalletRequest.builder()
                        .currency("BTC")
                        .address("1KQDe9wA57sEJ2ue52kupuMwRoGjNvjHD6")
                        .build(),
                WalletRequest.builder()
                        .currency("DOGE1")
                        .address("D7xoS7GdHS4fRZKgcD63u93mYjYSupgtzL")
                        .build(),
                WalletRequest.builder()
                        .currency("DOGE")
                        .address("D7xoS7GdHS4fRZKgcD63u93mYjYSupgtzL")
                        .build(),
                WalletRequest.builder()
                        .currency("WAVES")
                        .address("3PKhy3XfjhVw281vzxWypHHTLWkTmb91Zcp")
                        .build()
        ).collect(Collectors.toList());
        long l1 = System.currentTimeMillis();
        List<Balance> walletBalances = proxyAsyncCryptoCurrencyBalanceResolver.getWalletBalances(req);
        log.debug("walletBalances: {}", walletBalances);
        log.warn("Time in ms: {}", System.currentTimeMillis() - l1);
        assert walletBalances.size() == 4;
        assert walletBalances.stream()
                .allMatch(x-> Objects.nonNull(x.getValue()) && Objects.nonNull(x.getDivider()));
    }

}
