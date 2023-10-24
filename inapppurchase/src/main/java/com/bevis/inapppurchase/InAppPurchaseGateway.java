package com.bevis.inapppurchase;

public interface InAppPurchaseGateway {
    PurchaseResponse loadPurchaseReceipt(InAppPurchaseProvider provider, PurchaseReceiptDTO purchaseReceiptDTO);
}
