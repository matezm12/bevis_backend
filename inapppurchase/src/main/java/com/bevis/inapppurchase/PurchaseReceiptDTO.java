package com.bevis.inapppurchase;

import lombok.Data;

import java.util.Map;

@Data
public class PurchaseReceiptDTO {
    private String productId;
    private String purchaseId;
    private Map<String, Object> params;
}
