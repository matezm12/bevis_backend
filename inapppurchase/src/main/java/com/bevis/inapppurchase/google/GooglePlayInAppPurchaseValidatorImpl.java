package com.bevis.inapppurchase.google;

import com.bevis.inapppurchase.*;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
class GooglePlayInAppPurchaseValidatorImpl implements InAppPurchaseValidator {

    private final GooglePlayInAppPurchaseProps purchaseProps;
    private final AndroidPublisher androidPublisher;

    GooglePlayInAppPurchaseValidatorImpl(GooglePlayInAppPurchaseProps purchaseProps,
                                         @Autowired(required = false) AndroidPublisher androidPublisher) {
        this.purchaseProps = purchaseProps;
        this.androidPublisher = androidPublisher;
    }

    @Override
    public InAppPurchaseProvider getProvider() {
        return InAppPurchaseProvider.GOOGLE_PLAY;
    }

    @Override
    public PurchaseResponse loadPurchaseReceipt(PurchaseReceiptDTO purchaseReceiptDTO) {
        log.debug("Validating purchase: {}", purchaseReceiptDTO);
        try {
            Map<String, Object> params = purchaseReceiptDTO.getParams();
            if (Objects.isNull(params) || !params.containsKey("purchaseToken")) {
                log.error("PurchaseToken missed");
                throw new InAppPurchaseException("PurchaseToken missed");
            }
            Object purchaseTokenParam = params.get("purchaseToken");
            if (!(purchaseTokenParam instanceof String)) {
                log.error("PurchaseToken should be text value");
                throw new InAppPurchaseException("PurchaseToken should be text value");
            }
            String purchaseToken = purchaseTokenParam.toString();
            ProductPurchase purchase = androidPublisher.purchases().products().get(
                    purchaseProps.getPackageName(),
                    purchaseReceiptDTO.getProductId(),
                    purchaseToken
            ).execute();
            log.debug("Purchase data loaded from PlayMarket: {}", purchase.toPrettyString());
            if (!Objects.equals(purchase.getOrderId(), purchaseReceiptDTO.getPurchaseId())) {
                log.error("Purchase ID from Market: {} and from request: {} are not equals", purchase.getOrderId(), purchaseReceiptDTO.getPurchaseId());
                throw new InAppPurchaseException("Purchase ID from Market and from request are not equals");
            }
            return PurchaseResponse.of(purchase.getOrderId(), Instant.ofEpochMilli(purchase.getPurchaseTimeMillis()));
        } catch (GoogleJsonResponseException e) {
            log.error(e.getMessage());
            throw new InAppPurchaseException("Failed to validate purchase. " + e.getDetails().getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InAppPurchaseException("Failed to validate purchase. " + e.getMessage(), e);
        }
    }

}
