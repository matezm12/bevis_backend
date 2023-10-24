package com.bevis.exchangedata.impl;

import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.blockchaincore.BlockchainRepository;
import com.bevis.exchangedata.CryptoPriceLoader;
import com.bevis.exchangedata.CurrencyExchangeLoader;
import com.bevis.exchangedata.dto.CoinPriceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
class CryptoPriceLoaderImpl implements CryptoPriceLoader {

    private final BlockchainRepository blockchainRepository;
    private final CurrencyExchangeLoader currencyExchangeLoader;

    @Override
    public List<CoinPriceDTO> findAll(String fiatCurrency) {
        Set<String> cryptoCurrencies = blockchainRepository.findAll()
                .stream()
                .map(Blockchain::getCurrencyCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return currencyExchangeLoader.getExchangeRateForCurrencies(cryptoCurrencies, fiatCurrency)
                .stream()
                .map(x -> CoinPriceDTO.builder()
                        .cryptoCurrency(x.getSourceCurrency())
                        .fiatCurrency(x.getDestinationCurrency())
                        .cryptoPrice(x.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
