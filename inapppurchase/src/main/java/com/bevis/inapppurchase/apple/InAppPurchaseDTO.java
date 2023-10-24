package com.bevis.inapppurchase.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class InAppPurchaseDTO {

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("original_transaction_id")
    private String originalTransactionId;

    @JsonProperty("purchase_date_ms")
    private String purchaseDateMilliseconds;

    @JsonProperty("original_purchase_date_ms")
    private String originalPurchaseDateMilliseconds;

    @JsonProperty("is_trial_period")
    private String isTrialPeriod;

}
