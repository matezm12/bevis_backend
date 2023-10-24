package com.bevis.credits.dto;

import com.bevis.credits.domain.enumeration.CreditsChargeSource;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargeCreditsHistoryItem extends CreditsHistoryItem {
    private BigDecimal purchasePrice; //in USD
    private CreditsChargeSource chargeType;
}
