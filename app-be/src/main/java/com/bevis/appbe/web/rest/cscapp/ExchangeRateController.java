package com.bevis.appbe.web.rest.cscapp;

import com.bevis.exchangedata.CryptoPriceLoader;
import com.bevis.exchangedata.dto.CoinPriceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExchangeRateController {

    private final CryptoPriceLoader cryptoPriceLoader;

    @GetMapping("exchange-rates/all-crypto")
    List<CoinPriceDTO> findAll(@RequestParam(value = "fiat-currency", defaultValue = "USD") String fiatCurrency) {
        log.debug("REST to load all supported crypto exchanges...");
        return cryptoPriceLoader.findAll(fiatCurrency);
    }
}
