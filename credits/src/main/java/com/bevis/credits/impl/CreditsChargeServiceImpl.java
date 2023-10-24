package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.credits.domain.CreditsCharge;
import com.bevis.credits.domain.CreditsPackage;
import com.bevis.user.domain.User;
import com.bevis.credits.domain.enumeration.CreditsChargeSource;
import com.bevis.credits.repository.CreditsChargeRepository;
import com.bevis.credits.repository.CreditsPackageRepository;
import com.bevis.credits.CreditsBalanceService;
import com.bevis.credits.CreditsChargeService;
import com.bevis.credits.exception.CreditsChargeException;
import com.bevis.credits.exception.DuplicateCreditsChargeException;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.inapppurchase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
class CreditsChargeServiceImpl implements CreditsChargeService {
    private final CreditsBalanceService creditsBalanceService;
    private final CreditsChargeRepository creditsChargeRepository;
    private final CreditsPackageRepository creditsPackageRepository;
    private final InAppPurchaseGateway inAppPurchaseGateway;

    @Transactional
    @Override
    public CreditsCharge freeChargeUser(User user, BigDecimal creditsAmount, String description) {
        CreditsBalance balance = creditsBalanceService.creditUser(user, creditsAmount);
        CreditsCharge creditsCharge = new CreditsCharge();
        creditsCharge.setDateTime(balance.getDateTime());
        creditsCharge.setCredits(creditsAmount);
        creditsCharge.setDescription(description);
        creditsCharge.setCreditsBalance(balance);
        creditsCharge.setSource(CreditsChargeSource.FREE_PLAN);
        return creditsChargeRepository.saveAndFlush(creditsCharge);
    }

    @Transactional
    @Override
    public CreditsBalance chargeUserWithInAppPurchaseProvider(User user, InAppPurchaseProvider provider, PurchaseReceiptDTO purchaseReceiptDTO) {
        log.trace("chargeUserWithInAppPurchaseProvider");
        String productId = purchaseReceiptDTO.getProductId();
        log.debug("Product ID: {}", productId);
        CreditsPackage creditsPackage = getCreditsPackageByProductId(productId);
        log.debug("CreditsPackage ID: {}", creditsPackage.getId());
        try {
            String purchaseId = purchaseReceiptDTO.getPurchaseId();
            validatePurchaseUnique(purchaseId);
            PurchaseResponse purchaseResponse = inAppPurchaseGateway.loadPurchaseReceipt(provider, purchaseReceiptDTO);
            validatePurchaseResponse(purchaseResponse, purchaseReceiptDTO);

            BigDecimal amountInUnits = creditsPackage.getAmountInUnits();
            CreditsBalance newBalance = creditsBalanceService.creditUser(user, amountInUnits);
            CreditsCharge creditsCharge = new CreditsCharge();
            creditsCharge.setDateTime(purchaseResponse.getDateTime());
            creditsCharge.setProductId(productId);
            creditsCharge.setPurchaseId(purchaseResponse.getPurchaseId());
            creditsCharge.setCredits(amountInUnits);
            creditsCharge.setSource(CreditsChargeSource.valueOf(provider.name()));
            creditsCharge.setDescription("Units purchased from " + provider.name());
            creditsCharge.setCreditsBalance(newBalance);
            creditsChargeRepository.saveAndFlush(creditsCharge);

            return newBalance;
        } catch (InAppPurchaseException e) {
            log.error("Purchase not valid. Provider: {}, purchase receipt: {}", provider, purchaseReceiptDTO);
            throw new CreditsChargeException(e.getMessage(), e);
        }
    }

    private CreditsPackage getCreditsPackageByProductId(String productId) {
        return creditsPackageRepository.findFirstByProductId(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID: {} not found", productId);
                    return new ObjectNotFoundException("Product with ID: " + productId + " not found");
                });
    }

    private void validatePurchaseUnique(String purchaseId) {
        if (creditsChargeRepository.existsByPurchaseId(purchaseId)) {
            log.error("Purchase with ID: {} already exists in database.", purchaseId);
            throw new DuplicateCreditsChargeException("This purchase already used.");
        }
    }

    private void validatePurchaseResponse(PurchaseResponse purchaseResponse, PurchaseReceiptDTO purchaseReceiptDTO) {
        if (Objects.isNull(purchaseResponse)) {
            log.error("Purchase not loaded. ");
            throw new CreditsChargeException("Purchase not valid.");
        }
    }
}
