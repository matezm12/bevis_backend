package com.bevis.appbe.web.rest.admin;

import com.bevis.credits.domain.CreditsPayment;
import com.bevis.credits.CreditsPaymentManagementService;
import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CreditsPaymentController {

    private final CreditsPaymentManagementService creditsPaymentManagementService;

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/credits-payments")
    DataResponse<CreditsPayment> findAll(Pageable pageable) {
        log.debug("REST to load credits-payments...");
        Page<CreditsPayment> page = creditsPaymentManagementService.findAll(pageable);
        return DataResponse.of(page);
    }
}
