package com.bevis.inapppurchase.apple;

import com.bevis.inapppurchase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
class AppStoreInAppPurchaseValidatorImpl implements InAppPurchaseValidator {

    private static final String LIVE_PURCHASE_VALIDATION_URL = "https://buy.itunes.apple.com/verifyReceipt";
    private static final String SANDBOX_PURCHASE_VALIDATION_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

    private static final Long SUCCESS_STATUS = 0L;
    private static final String RECEIPT_DATA_PARAM = "receiptData";

    private final AppStoreInAppPurchaseProps appStoreInAppPurchaseProps;
    private final RestTemplate restTemplate;

    @Override
    public InAppPurchaseProvider getProvider() {
        return InAppPurchaseProvider.APP_STORE;
    }

    @Override
    public PurchaseResponse loadPurchaseReceipt(PurchaseReceiptDTO purchaseReceiptDTO) {
        String purchaseValidationUrl = getPurchaseValidationUrl();
        String receiptData = getReceiptData(purchaseReceiptDTO);
        ResponseEntity<WrappedReceiptDTO> response = restTemplate.exchange(purchaseValidationUrl, HttpMethod.POST, getReceiptRequest(receiptData), WrappedReceiptDTO.class);
        validateResponse(response);
        WrappedReceiptDTO wrappedReceipt = response.getBody();
        assert wrappedReceipt != null;
        log.trace(wrappedReceipt.getEnvironment());
        ReceiptDTO receipt = wrappedReceipt.getReceipt();
        if (Objects.isNull(receipt)) {
            log.error("Can not load receipt");
            throw new InAppPurchaseException("Can not load receipt");
        }
        validateBundleId(receipt);
        List<InAppPurchaseDTO> inAppPurchases = receipt.getInAppPurchases();
        InAppPurchaseDTO inAppPurchase = findInAppPurchaseByProductIdAndPurchaseId(inAppPurchases, purchaseReceiptDTO);
        Instant instant = Instant.ofEpochMilli(Long.parseLong(inAppPurchase.getOriginalPurchaseDateMilliseconds()));
        String transactionId = inAppPurchase.getTransactionId();
        return PurchaseResponse.of(transactionId, instant);
    }

    private String getReceiptData(PurchaseReceiptDTO purchaseReceiptDTO) {
        Map<String, Object> params = purchaseReceiptDTO.getParams();
        if (Objects.isNull(params) || !params.containsKey(RECEIPT_DATA_PARAM)) {
            log.error("ReceiptData missed");
            throw new InAppPurchaseException("ReceiptData missed");
        }
        Object receiptDataParam = params.get(RECEIPT_DATA_PARAM);
        if (!(receiptDataParam instanceof String)) {
            log.error("receiptDataParam should be text value");
            throw new InAppPurchaseException("receiptDataParam should be text value");
        }
        return receiptDataParam.toString();
    }

    private void validateBundleId(ReceiptDTO receipt) {
        String bundleId = receipt.getBundleId();
        log.trace(bundleId);
        if (!Objects.equals(bundleId, appStoreInAppPurchaseProps.getBundleId())) {
            log.error("Actual bundle id: {}, but expected: {}", bundleId, appStoreInAppPurchaseProps.getBundleId());
            throw new InAppPurchaseException("Bundle ID not correct");
        }
    }

    private InAppPurchaseDTO findInAppPurchaseByProductIdAndPurchaseId(List<InAppPurchaseDTO> inAppPurchases, PurchaseReceiptDTO purchaseReceiptDTO) {
        return inAppPurchases.stream()
                .filter(x -> matchByProductIdAndPurchaseId(x, purchaseReceiptDTO.getProductId(), purchaseReceiptDTO.getPurchaseId()))
                .findAny()
                .orElseThrow(() -> {
                    log.error("InApp purchase {} not found", purchaseReceiptDTO.getPurchaseId());
                    return new InAppPurchaseException("Purchase not found");
                });
    }

    private boolean matchByProductIdAndPurchaseId(InAppPurchaseDTO inAppPurchase, String productId, String purchaseId) {
        return Objects.equals(inAppPurchase.getProductId(), productId) && Objects.equals(inAppPurchase.getTransactionId(), purchaseId);
    }

    private HttpEntity<ReceiptRequest> getReceiptRequest(String receiptData) {
        ReceiptRequest receiptRequest = new ReceiptRequest();
        receiptRequest.setReceiptData(receiptData);
        HttpHeaders headers = getHeaders();
        return new HttpEntity<>(receiptRequest, headers);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private void validateResponse(ResponseEntity<WrappedReceiptDTO> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            WrappedReceiptDTO wrappedReceipt = response.getBody();
            if (Objects.isNull(wrappedReceipt)) {
                log.error("Can not load receipt response");
                throw new InAppPurchaseException("Can not load receipt response");
            }
            if (!Objects.equals(wrappedReceipt.getStatus(), SUCCESS_STATUS)) {
                String errorStatus = checkStatus(wrappedReceipt.getStatus());
                log.error("Error loading purchase receipt with error: {}", errorStatus);
                throw new InAppPurchaseException(errorStatus);
            }
        } else {
            log.error("Receipt response failed with error: {}", response.toString());
            throw new InAppPurchaseException(response.toString());
        }
    }

    private String checkStatus(Long status) {
        String msg;
        switch (status.intValue()) {
            case 21000:
                msg = "The App Store could not read the JSON object you provided";
                log.info("\n  21000 : The App Store could not read the JSON object you provided. ");
                break;
            case 21002:
                msg = "The data in the receipt-data property was malformed.";
                log.info("\n  21002 : The data in the receipt-data property was malformed..   ");
                break;
            case 21003:
                msg = "The data in the receipt-data property was malformed.";
                log.info("\n  21003 : The receipt could not be authenticated. ");
                break;
            case 21004:
                msg = "TThe shared secret you provided does not match the shared secret on file for your account.";
                log.info("\n  21004 : The shared secret you provided does not match the shared secret on file for your account. ");
                break;
            case 21005:
                msg = "The receipt server is not currently available.";
                log.info("\n  21005 : The receipt server is not currently available. ");
                break;
            case 21006:
                msg = "This receipt is valid but the subscription has expired. When this status code is returned to your server, the receipt data is also decoded and returned as part of the response.";
                log.info("\n  21006 : This receipt is valid but the subscription has expired. When this status code is returned to your server, the receipt data is also decoded and returned as part of the response. ");
                break;
            case 21007:
                msg = "This receipt is a sandbox receipt, but it was sent to the production service for verification.";
                log.info("\n  21007 : This receipt is a sandbox receipt, but it was sent to the production service for verification. ");
                break;
            case 21008:
                msg = "This receipt is a production receipt, but it was sent to the sandbox service for verification.";
                log.info("\n  21008 : This receipt is a production receipt, but it was sent to the sandbox service for verification. ");
                break;

            default:
                msg = "Active subscription.";
                log.info("\n  0 : valid ....Active subscription. ");
                break;
        }
        return msg;
    }

    private String getPurchaseValidationUrl() {
        String purchaseMode = appStoreInAppPurchaseProps.getPurchaseMode();
        switch (purchaseMode) {
            case "buy":
                return LIVE_PURCHASE_VALIDATION_URL;
            case "sandbox":
                return SANDBOX_PURCHASE_VALIDATION_URL;
            default:
                throw new RuntimeException("Purchase mode not found");
        }
    }
}
