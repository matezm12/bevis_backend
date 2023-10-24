package com.bevis.bevisassetpush;

import com.bevis.credits.domain.CreditsPayment;
import com.bevis.bevisassetpush.dto.PriceCalculatingData;

import java.math.BigDecimal;

public interface PriceCalculatingService {
    CreditsPayment makePaymentUserForDocumentUpload(PriceCalculatingData data);
    BigDecimal calculatePriceInCreditCentsForUser(PriceCalculatingData data);
}
