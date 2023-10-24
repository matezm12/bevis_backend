package com.bevis.credits;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.credits.domain.CreditsCharge;
import com.bevis.user.domain.User;
import com.bevis.inapppurchase.InAppPurchaseProvider;
import com.bevis.inapppurchase.PurchaseReceiptDTO;

import java.math.BigDecimal;

public interface CreditsChargeService {

    CreditsCharge freeChargeUser(User user, BigDecimal creditsAmount, String description);

    CreditsBalance chargeUserWithInAppPurchaseProvider(User user, InAppPurchaseProvider provider, PurchaseReceiptDTO purchaseReceiptDTO);
}
