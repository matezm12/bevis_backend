package com.bevis.inapppurchase.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class ReceiptDTO {

    @JsonProperty("bundle_id")
    private String bundleId;

    @JsonProperty("in_app")
    private List<InAppPurchaseDTO> inAppPurchases;
}
