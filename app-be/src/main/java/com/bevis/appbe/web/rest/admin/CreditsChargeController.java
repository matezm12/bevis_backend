package com.bevis.appbe.web.rest.admin;

import com.bevis.credits.domain.CreditsCharge;
import com.bevis.credits.CreditsChargeManagementService;
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
public class CreditsChargeController {

    private final CreditsChargeManagementService creditsChargeManagementService;

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/credits-charges")
    DataResponse<CreditsCharge> findAll(Pageable pageable) {
        log.debug("REST to load credits-charges...");
        Page<CreditsCharge> page = creditsChargeManagementService.findAll(pageable);
        return DataResponse.of(page);
    }
}
