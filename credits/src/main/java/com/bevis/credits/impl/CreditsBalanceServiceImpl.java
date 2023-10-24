package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.events.dto.user.UserDataDeleteEvent;
import com.bevis.user.domain.User;
import com.bevis.credits.repository.CreditsBalanceRepository;
import com.bevis.credits.exception.CreditBalanceException;
import com.bevis.credits.CreditsBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
class CreditsBalanceServiceImpl implements CreditsBalanceService {

    private final CreditsBalanceRepository creditsBalanceRepository;

    @Transactional(readOnly = true)
    @Override
    public CreditsBalance getUserBalance(User user) {
        return tryToGetBalance(user)
                .orElseThrow(() -> new CreditBalanceException("Balance not found"));
    }

    @Override
    public CreditsBalance creditUser(User user, BigDecimal creditsAmount) {
        CreditsBalance balance = createNewCreditsBalance(user);

        CreditsBalance userBalance = tryToGetBalance(user).orElse(null);

        BigDecimal oldBalance = userBalance != null ? userBalance.getBalance() : BigDecimal.ZERO;
        balance.setBalance(oldBalance.add(creditsAmount));

        BigDecimal oldTotalIncome = userBalance != null ? userBalance.getTotalIncome() : BigDecimal.ZERO;
        balance.setTotalIncome(oldTotalIncome.add(creditsAmount));

        BigDecimal oldTotalOutcome = userBalance != null ? userBalance.getTotalOutcome() : BigDecimal.ZERO;
        balance.setTotalOutcome(oldTotalOutcome);


        return creditsBalanceRepository.saveAndFlush(balance);
    }

    @Transactional
    @Override
    public CreditsBalance creditUser(User user, Long creditsAmountInCents) {
        BigDecimal creditsAmountInCredits = BigDecimal.valueOf(creditsAmountInCents).divide(BigDecimal.valueOf(100));
        return creditUser(user, creditsAmountInCredits);
    }

    @Override
    public CreditsBalance debitUser(User user, BigDecimal creditsAmount) {
        CreditsBalance balance = createNewCreditsBalance(user);

        CreditsBalance userBalance = tryToGetBalance(user).orElse(null);

        BigDecimal oldBalance = userBalance != null ? userBalance.getBalance() : BigDecimal.ZERO;
        balance.setBalance(oldBalance.subtract(creditsAmount));

        BigDecimal oldTotalIncome = userBalance != null ? userBalance.getTotalIncome() : BigDecimal.ZERO;
        balance.setTotalIncome(oldTotalIncome);

        BigDecimal oldTotalOutcome = userBalance != null ? userBalance.getTotalOutcome() : BigDecimal.ZERO;
        balance.setTotalOutcome(oldTotalOutcome.add(creditsAmount));

        return creditsBalanceRepository.save(balance);
    }

    @Transactional
    @Override
    public CreditsBalance debitUser(User user, Long creditsAmountInCents) {
        BigDecimal creditsAmountInCredits = BigDecimal.valueOf(creditsAmountInCents).divide(BigDecimal.valueOf(100));
        return debitUser(user, creditsAmountInCredits);
    }

    @EventListener
    @Order(2)
    @Override
    public void handleUserDataDeleteEvent(UserDataDeleteEvent e) {
        log.debug("Deleting Credits balance User data. [Order 2.]");
        creditsBalanceRepository.deleteAllByUserId(e.getUserId());
    }

    private CreditsBalance createNewCreditsBalance(User user) {
        CreditsBalance balance = new CreditsBalance();
        Instant timestamp = Instant.now();
        balance.setDateTime(timestamp);
        balance.setUser(user);
        return balance;
    }

    private Optional<CreditsBalance> tryToGetBalance(User user) {
        return creditsBalanceRepository.findFirstByUserOrderByDateTimeDesc(user);
    }
}
