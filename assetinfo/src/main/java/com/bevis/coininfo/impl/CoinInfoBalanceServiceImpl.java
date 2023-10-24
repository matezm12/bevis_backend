package com.bevis.coininfo.impl;

import com.bevis.balance.dto.Balance;
import com.bevis.balance.exception.CryptoBalanceException;
import com.bevis.balancecore.CryptoBalanceLoader;
import com.bevis.coininfo.CoinInfoBalanceService;
import com.bevis.coininfo.dto.CoinInfo;
import com.bevis.exchange.dto.CurrencyExchangeRate;
import com.bevis.exchangedata.CurrencyExchangeLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bevis.balance.util.FiatCurrencyUtil.formatFiatCurrencyBalance;

@Service
@Slf4j
@RequiredArgsConstructor
class CoinInfoBalanceServiceImpl implements CoinInfoBalanceService {

    private final CryptoBalanceLoader cryptoBalanceLoader;
    private final CurrencyExchangeLoader currencyExchangeLoader;

    @Override
    public CoinInfo getCoinInfo(String publicKey, String coinCurrency, String currency) {
        CoinInfo.CoinInfoBuilder builder = CoinInfo.builder();

        try {
            CurrencyExchangeRate exchangeRate = currencyExchangeLoader.getExchangeRateForCurrencies(coinCurrency, currency);
            log.debug("currency {}:{} - {}", coinCurrency, currency, exchangeRate.getValue());

            Balance walletBalance = cryptoBalanceLoader.getWalletBalance(publicKey, coinCurrency);

            Double balanceInSelectedCurrency = walletBalance.getRealBalanceNullable()
                    .map(balance -> balance * exchangeRate.getValue()).orElse(null);
            log.debug("wallet {} balanceInSelectedCurrency {}", publicKey, balanceInSelectedCurrency);

            Double fiatBalanceFormatted = formatFiatCurrencyBalance(balanceInSelectedCurrency);
            log.debug("Formatted fiat balance: {}", fiatBalanceFormatted);

            builder
                    .exchangeRate(exchangeRate.getValue())
                    .sourceCurrencyBalance(walletBalance.getRealBalanceNullable().orElse(0.0))
                    .destinationCurrencyBalance(Optional.ofNullable(balanceInSelectedCurrency).orElse(0.0))
                    .sourceBalance(walletBalance.getRealBalanceNullable()
                            .map(balance -> balance + " " + coinCurrency)
                            .orElse("N/A " + coinCurrency) )
                    .destinationBalance(currency.toUpperCase() + " " + Optional.ofNullable(fiatBalanceFormatted)
                            .map(Object::toString)
                            .orElse("N/A"));

        } catch (CryptoBalanceException e){
            builder
                    .sourceCurrencyBalance(null)
                    .destinationCurrencyBalance(null)
                    .sourceBalance("Balance not available")
                    .destinationBalance("N/A");
            log.error("Error loading balance of coin. ", e);
        }

        builder
                .hasTokens(false)
                .sourceCurrency(coinCurrency)
                .destinationCurrency(currency.toUpperCase());

        return builder.build();
    }

}
