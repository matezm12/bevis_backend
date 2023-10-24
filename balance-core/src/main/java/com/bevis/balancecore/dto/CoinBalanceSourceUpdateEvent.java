package com.bevis.balancecore.dto;

import com.bevis.balancecore.domain.CoinBalanceSource;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinBalanceSourceUpdateEvent {
    private CoinBalanceSource coinBalanceSource;
}
