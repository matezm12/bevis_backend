package com.bevis.nft;

import com.bevis.nft.dto.Balance;
import com.bevis.nft.slp.ElectronCashSlpProxyService;
import com.bevis.nft.slp.dto.SendTokensRequest;
import com.bevis.nft.slp.dto.SendTokensResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BitcoinCashSlpServiceImpl implements BitcoinCashSlpService {

    private final ElectronCashSlpProxyService electronCashSlpProxyService;

    @Override
    public Balance getBalance() {
        Balance balance = new Balance();
        balance.setConfirmed(electronCashSlpProxyService.getBalance().getConfirmed());
        return balance;
    }

    @Override
    public SendTokensResponse sendTokens(SendTokensRequest request) {
        return electronCashSlpProxyService.sendTokens(request);
    }
}
