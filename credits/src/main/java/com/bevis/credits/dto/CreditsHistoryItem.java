package com.bevis.credits.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreditsHistoryItem {
    private ActionType itemType;
    private Instant dateTime;
    private BigDecimal balanceInUnits;
    private BigDecimal valueInUnits;
}
