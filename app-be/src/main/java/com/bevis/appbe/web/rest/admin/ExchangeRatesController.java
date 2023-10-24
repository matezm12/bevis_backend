package com.bevis.appbe.web.rest.admin;

import com.bevis.exchangedata.domain.ExchangeRate;
import com.bevis.exchangedata.ExchangeRatesManagementService;
import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExchangeRatesController {

    private final ExchangeRatesManagementService exchangeRatesManagementService;

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/exchange-rates")
    DataResponse<ExchangeRate> findAll(@RequestParam(value = "search", required = false) String search, Pageable pageable) {
        log.debug("REST to load fx-rates...");
        Page<ExchangeRate> page = exchangeRatesManagementService.findAllByCurrencyCode(search, pageable);
        return DataResponse.of(page);
    }

    @Secured(ADMIN)
    @PostMapping("admin/exchange-rates/update-crypto")
    void updateCryptoExchangeRatesManually() {
        log.debug("REST to update crypto-rates...");
        exchangeRatesManagementService.updateCryptoExchangeRatesManually();
    }

    @Secured(ADMIN)
    @PostMapping("admin/exchange-rates/update-fiat")
    void updateFiatExchangeRatesManually() {
        log.debug("REST to update fiat-rates...");
        exchangeRatesManagementService.updateFiatExchangeRatesManually();
    }
}
