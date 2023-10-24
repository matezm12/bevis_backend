package com.bevis.appbe.web.rest.bevisapp;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.credits.domain.CreditsPackage;
import com.bevis.user.domain.User;
import com.bevis.credits.CreditsBalanceService;
import com.bevis.credits.CreditsChargeService;
import com.bevis.credits.CreditsHistoryService;
import com.bevis.credits.CreditsPackageService;
import com.bevis.credits.dto.CreditsHistoryItem;
import com.bevis.user.UserService;
import com.bevis.inapppurchase.InAppPurchaseProvider;
import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.appbe.web.rest.vm.PurchaseReceiptVm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.USER;

@Api(description = "Credits Controller")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CreditsController {

    private final UserService userService;
    private final CreditsBalanceService creditsBalanceService;
    private final CreditsHistoryService creditsHistoryService;
    private final CreditsPackageService creditsPackageService;
    private final CreditsChargeService creditsChargeService;

    @ApiOperation(value = "Get current user balance")
    @Secured({ADMIN, USER})
    @GetMapping("balance")
    CreditsBalance getBalance() {
        return creditsBalanceService.getUserBalance(userService.getCurrentUser());
    }

    @ApiOperation(value = "Get history of user credits actions")
    @Secured({ADMIN, USER})
    @ApiPageable
    @GetMapping("balance/history")
    DataResponse<CreditsHistoryItem> getBalanceHistory(Pageable pageable) {
        return DataResponse.of(creditsHistoryService.getUserBalanceHistory(userService.getCurrentUser(), pageable));
    }

    @ApiOperation(value = "Loading list of Units packages for Buying.")
    @Secured({ADMIN, USER})
    @GetMapping("credits-packages")
    List<CreditsPackage> getCreditsPackages() {
        return creditsPackageService.getCreditsPackages();
    }

    @ApiOperation(value = "Charging user balance using In-App purchases.")
    @Secured({ADMIN, USER})
    @PostMapping("balance/charge")
    CreditsBalance chargeUserBalance(@RequestBody @Valid PurchaseReceiptVm purchaseReceiptVm) {
        log.debug("Preparing charge UserBalance: {}", purchaseReceiptVm);
        User currentUser = userService.getCurrentUser();
        @NotNull InAppPurchaseProvider provider = purchaseReceiptVm.getProvider();
        return creditsChargeService.chargeUserWithInAppPurchaseProvider(currentUser, provider, purchaseReceiptVm);
    }

}
