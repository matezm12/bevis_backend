package com.bevis.exchange.crypto;

import com.bevis.exchange.ExchangeConstants;
import com.bevis.exchange.ExchangeProps;
import com.bevis.exchange.dto.CurrencyExchangeRate;
import com.bevis.exchange.exception.ExchangeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static com.bevis.common.util.StringUtil.toLowerCase;
import static com.bevis.common.util.SenderUtil.sendGetRequest;

@Service
@Slf4j
@RequiredArgsConstructor
class CryptoCurrencyExchangeServiceImpl implements CryptoCurrencyExchangeService {

    public static final String EXCHANGE_ROUTE_TEMPLATE = "/exchange/crypto/%s-%s";
    public static final String USD_EXCHANGE_ROUTE_TEMPLATE = "/exchange/crypto/usd-base/%s";
    public static final String API_KEY_HEADER = "x-api-key";

    private final ExchangeProps exchangeProps;

    @Override
    public CurrencyExchangeRate getExchangeRateForCurrencies(String sourceCurrency, String destinationCurrency) {
        try {
            return getCryptoCurrencyExchangeRate(sourceCurrency, destinationCurrency);
        } catch (Exception e) {
            log.warn("Currency exchange cryptoCurrencyExchangeService not found {}-{}  exchange. Tried to find via fiatCurrencyExchangeService", sourceCurrency, destinationCurrency);
            return CurrencyExchangeRate.builder().build();
        }
    }

    @Override
    public CurrencyExchangeRate getUsdExchangeRateForCurrency(String sourceCurrency) {
        final String destinationCurrency = ExchangeConstants.BASE_CURRENCY;
        try {
            final String route = String.format(USD_EXCHANGE_ROUTE_TEMPLATE, toLowerCase(sourceCurrency));
            final String url = exchangeProps.getBaseUrl() + route;
            log.debug("url: {}", url);
            return loadExchangeDataFromApi(sourceCurrency, destinationCurrency, url);
        } catch (Exception e) {
            log.warn("Currency exchange cryptoCurrencyExchangeService not found {}-{}  exchange. Tried to find via fiatCurrencyExchangeService", sourceCurrency, destinationCurrency);
            return CurrencyExchangeRate.builder().build();
        }
    }

    private CurrencyExchangeRate getCryptoCurrencyExchangeRate(String sourceCurrency, String destinationCurrency) {
        String route = String.format(EXCHANGE_ROUTE_TEMPLATE, sourceCurrency, destinationCurrency);
        String url =  exchangeProps.getBaseUrl() + route;
        log.debug("url: {}", url);
        return loadExchangeDataFromApi(sourceCurrency, destinationCurrency, url);
    }

    private CurrencyExchangeRate loadExchangeDataFromApi(String sourceCurrency, String destinationCurrency, String apiUrl) {
        Map<String, String> headers = Collections.singletonMap(API_KEY_HEADER, exchangeProps.getApiKey());
        try {
            CryptoExchangeDTO cryptoExchangeDTO = sendGetRequest(apiUrl, CryptoExchangeDTO.class, headers);
            log.debug("Currency exchange {}-{} is {}", sourceCurrency, destinationCurrency, cryptoExchangeDTO.getRate());
            return new CurrencyExchangeRate(cryptoExchangeDTO.getBase(), cryptoExchangeDTO.getTarget(), cryptoExchangeDTO.getRate());
        } catch (Exception e) {
            log.error("Error loading currencies exchange {}-{}, cause {}", sourceCurrency, destinationCurrency, e.getMessage());
            throw new ExchangeException("Error loading currencies exchange " + sourceCurrency + "-" + destinationCurrency, e);
        }
    }
}
