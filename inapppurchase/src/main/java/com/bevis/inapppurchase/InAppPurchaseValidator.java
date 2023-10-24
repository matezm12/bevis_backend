package com.bevis.inapppurchase;

public interface InAppPurchaseValidator {
    InAppPurchaseProvider getProvider();
    PurchaseResponse loadPurchaseReceipt(PurchaseReceiptDTO purchaseReceiptDTO);
}
