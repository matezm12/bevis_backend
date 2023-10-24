package com.bevis.inapppurchase.apple;

import lombok.Data;

@Data
class WrappedReceiptDTO {
    private ReceiptDTO receipt;
    private Long status;
    private String environment;
}
