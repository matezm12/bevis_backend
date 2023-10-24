package com.bevis.appbe.web.rest.vm;

import com.bevis.inapppurchase.InAppPurchaseProvider;
import com.bevis.inapppurchase.PurchaseReceiptDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PurchaseReceiptVm extends PurchaseReceiptDTO {

    @NotNull
    private InAppPurchaseProvider provider;
}
