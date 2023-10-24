package com.bevis.inapppurchase.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class ReceiptRequest {
    @JsonProperty("receipt-data")
    private String receiptData;
}
