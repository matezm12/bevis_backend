package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.credits.domain.CreditsPayment;
import com.bevis.user.domain.User;
import com.bevis.credits.repository.CreditsPaymentRepository;
import com.bevis.credits.CreditsBalanceService;
import com.bevis.credits.CreditsPaymentService;
import com.bevis.credits.exception.CreditPaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
class CreditsPaymentServiceImpl implements CreditsPaymentService {

    private final CreditsPaymentRepository creditsPaymentRepository;
    private final CreditsBalanceService creditsBalanceService;
    private final CreditsProps creditsProps;

    @Override
    public CreditsPayment makePaymentForUser(User user, BigDecimal creditsAmount, String description) {
        BigDecimal realCreditsAmount;
        if (creditsProps.getPaymentsEnabled()) {
            realCreditsAmount = creditsAmount;
        } else {
            realCreditsAmount = BigDecimal.valueOf(0);
        }

        CreditsBalance userBalance = creditsBalanceService.getUserBalance(user);
        BigDecimal balance = userBalance.getBalance();
        if (balance.compareTo(realCreditsAmount) < 0) {
            log.error("Insufficient funds");
            throw new CreditPaymentException("Insufficient funds");
        }
        CreditsBalance newBalance = creditsBalanceService.debitUser(user, realCreditsAmount);
        CreditsPayment creditsPayment = new CreditsPayment();
        creditsPayment.setDateTime(newBalance.getDateTime());
        creditsPayment.setCredits(realCreditsAmount);
        creditsPayment.setDescription(description);
        creditsPayment.setCreditsBalance(newBalance);
        return creditsPaymentRepository.saveAndFlush(creditsPayment);
    }

}
