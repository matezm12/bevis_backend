package com.bevis.balance.impl;

import com.bevis.balance.AsyncCryptoCurrencyBalanceResolver;
import com.bevis.balance.CryptoCurrencyBalanceResolver;
import com.bevis.balance.dto.Balance;
import com.bevis.balance.dto.WalletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
@Slf4j
@RequiredArgsConstructor
class ProxyAsyncCryptoCurrencyBalanceResolverImpl implements CryptoCurrencyBalanceResolver {

    private final AsyncCryptoCurrencyBalanceResolver asyncCryptoCurrencyBalanceResolver;
    private final BalanceMapper balanceMapper;


    @Override
    public List<Balance> getWalletBalances(List<WalletRequest> walletRequests) {
        return balanceMapper.toBalanceList(
                asyncCryptoCurrencyBalanceResolver.loadBalances(
                        balanceMapper.mapRequest(walletRequests)
                )
        );
    }

    @Override
    public List<Balance> getWalletBalances(String currency, List<String> addresses, String source) {
        return balanceMapper.toBalanceList(asyncCryptoCurrencyBalanceResolver.loadBalances(currency, addresses, source));
    }
}
