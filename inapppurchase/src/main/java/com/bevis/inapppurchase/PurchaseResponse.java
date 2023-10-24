package com.bevis.inapppurchase;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor(staticName = "of")
public class PurchaseResponse {
    private String purchaseId;
    private Instant dateTime;
}
