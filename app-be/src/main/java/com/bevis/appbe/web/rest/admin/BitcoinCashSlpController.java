package com.bevis.appbe.web.rest.admin;

import com.bevis.nft.BitcoinCashSlpService;
import com.bevis.nft.dto.Balance;
import com.bevis.nft.slp.dto.SendTokensRequest;
import com.bevis.nft.slp.dto.SendTokensResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class BitcoinCashSlpController {

    private final BitcoinCashSlpService bitcoinCashSlpService;

    @Secured(ADMIN)
    @GetMapping("admin/bitcoin-cash-slp/balance")
    Balance getBalance() {
        log.debug("REST to load bitcoin-cash-slp balance...");
        return bitcoinCashSlpService.getBalance();
    }

    @Secured(ADMIN)
    @PostMapping("admin/bitcoin-cash-slp/send-tokens")
    SendTokensResponse sendTokens(@RequestBody SendTokensRequest request) {
        log.debug("REST to send bitcoin-cash-slp tokens ...");
        return bitcoinCashSlpService.sendTokens(request);
    }
}
