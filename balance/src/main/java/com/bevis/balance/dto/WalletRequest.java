package com.bevis.balance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletRequest {
    private String currency;
    private String address;
}
