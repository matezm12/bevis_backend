package com.bevis.credits;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.events.dto.user.UserDataDeleteEvent;
import com.bevis.user.domain.User;

import java.math.BigDecimal;

public interface CreditsBalanceService {
    CreditsBalance getUserBalance(User user);

    CreditsBalance creditUser(User user, BigDecimal creditsAmount);

    CreditsBalance creditUser(User user, Long creditsAmountInCents);

    CreditsBalance debitUser(User user, BigDecimal creditsAmount);

    CreditsBalance debitUser(User user, Long creditsAmountInCents);

    void handleUserDataDeleteEvent(UserDataDeleteEvent e);
}
