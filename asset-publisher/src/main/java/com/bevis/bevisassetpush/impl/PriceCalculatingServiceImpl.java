package com.bevis.bevisassetpush.impl;

import com.bevis.bevisassetpush.exception.BevisAssetPushException;
import com.bevis.bevisassetpush.PriceCalculatingService;
import com.bevis.credits.domain.CreditsBalance;
import com.bevis.credits.domain.CreditsPayment;
import com.bevis.credits.CreditsBalanceService;
import com.bevis.credits.CreditsPaymentService;
import com.bevis.bevisassetpush.dto.PriceCalculatingData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
class PriceCalculatingServiceImpl implements PriceCalculatingService {

    private final CreditsBalanceService creditsBalanceService;
    private final CreditsPaymentService creditsPaymentService;

    @Override
    public BigDecimal calculatePriceInCreditCentsForUser(PriceCalculatingData data) {
        long fileSizeInBytes = data.getFileParameters().getFileSizeInBytes();
        final long megabyte = 1024 * 1024;
        BigDecimal priceInCreditCents =  BigDecimal.valueOf(1)
                .multiply(BigDecimal.valueOf(fileSizeInBytes))
                .divide(BigDecimal.valueOf(megabyte));
        if (priceInCreditCents.equals(BigDecimal.ZERO)){
            priceInCreditCents = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(100));
        }
        CreditsBalance userBalance = creditsBalanceService.getUserBalance(data.getUser());
        BigDecimal balance = userBalance.getBalance();
        if (balance.compareTo(priceInCreditCents) < 0) {
            log.error("Insufficient funds");
            throw new BevisAssetPushException("Insufficient funds");
        }
        return priceInCreditCents;
    }

    @Override
    public CreditsPayment makePaymentUserForDocumentUpload(PriceCalculatingData data){
        BigDecimal priceInCreditCents = calculatePriceInCreditCentsForUser(data);
        return creditsPaymentService.makePaymentForUser(data.getUser(), priceInCreditCents, "User payment for Asset ID: " + data.getAssetId());
    }
}
