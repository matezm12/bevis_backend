package com.bevis.exchange.fiat;

import com.bevis.exchange.ExchangeProps;
import com.bevis.exchange.dto.CurrencyExchangeRate;
import com.bevis.exchange.exception.ExchangeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bevis.common.util.SenderUtil.sendGetRequest;

@Service
@Slf4j
@RequiredArgsConstructor
class FiatCurrencyExchangeServiceImp implements FiatCurrencyExchangeService {

    public static final String EXCHANGE_ROUTE_TEMPLATE = "/exchange/fiat/%s";
    public static final String API_KEY_HEADER = "x-api-key";

    private final ExchangeProps exchangeProps;

    @Override
    public CurrencyExchangeRate getExchangeRateForCurrencies(String sourceCurrency, String destinationCurrency) {
        return getExchangeRates(sourceCurrency)
                .stream()
                .filter(x -> Objects.equals(x.getCurrency(), destinationCurrency.toUpperCase()))
                .map(x -> mapRate(sourceCurrency.toUpperCase(), x))
                .findFirst()
                .orElseThrow(() -> new ExchangeException("Destination currency not found: " + destinationCurrency.toUpperCase()));
    }

    @Override
    public List<CurrencyExchangeRate> getExchangeRatesFromCurrency(String currency) {
        return getExchangeRates(currency)
                .stream()
                .map(x -> mapRate(currency, x))
                .collect(Collectors.toList());
    }

    private CurrencyExchangeRate mapRate(String currency, RateItemDTO x) {
        return new CurrencyExchangeRate(currency, x.getCurrency(), x.getRate());
    }

    private List<RateItemDTO> getExchangeRates(String currency) {
        String url = getUrl(currency);
        log.debug("url: {}", url);
        Map<String, String> headers = Collections.singletonMap(API_KEY_HEADER, exchangeProps.getApiKey());
        try {
            FiatExchangesDTO fiatExchangesDTO = sendGetRequest(url, FiatExchangesDTO.class, headers);
            List<RateItemDTO> rates = fiatExchangesDTO.getRates();
            log.info("Loaded {} currency exchanges for currency {}", rates.size(), currency);
            return rates;
        } catch (Exception e) {
            log.error("Error loading currency exchanges for currency {}, cause {}", currency, e.getMessage());
            throw new ExchangeException("Error loading currency exchanges for currency " + currency, e);
        }
    }

    private String getUrl(String baseCurrency) {
        String route = String.format(EXCHANGE_ROUTE_TEMPLATE, baseCurrency);
        return exchangeProps.getBaseUrl() + route;
    }
}
