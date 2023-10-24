package com.bevis.inapppurchase.impl;

import com.bevis.inapppurchase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
@RequiredArgsConstructor
class InAppPurchaseGatewayImpl implements InAppPurchaseGateway {

    private Map<InAppPurchaseProvider, InAppPurchaseValidator> inAppPurchaseValidators;

    @Override
    public PurchaseResponse loadPurchaseReceipt(InAppPurchaseProvider provider, PurchaseReceiptDTO purchaseReceiptDTO) {
        InAppPurchaseValidator inAppPurchaseValidator = getValidatorByProvider(provider);
        return inAppPurchaseValidator.loadPurchaseReceipt(purchaseReceiptDTO);
    }

    private InAppPurchaseValidator getValidatorByProvider(InAppPurchaseProvider provider) {
        if (inAppPurchaseValidators.containsKey(provider)){
            return inAppPurchaseValidators.get(provider);
        } else {
            log.error("Provider {} not registered", provider);
            throw new InAppPurchaseException("Provider not registered");
        }
    }

    @Autowired
    void initInAppPurchaseValidators(List<InAppPurchaseValidator> validatorsList){
        inAppPurchaseValidators = validatorsList.stream()
                .collect(toMap(InAppPurchaseValidator::getProvider, x -> x));
    }
}
