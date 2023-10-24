package com.bevis.credits;

import com.bevis.credits.domain.CreditsPayment;
import com.bevis.user.domain.User;

import java.math.BigDecimal;

public interface CreditsPaymentService {

    CreditsPayment makePaymentForUser(User user, BigDecimal creditsAmount, String description);
}
