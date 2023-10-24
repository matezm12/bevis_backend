package com.bevis.credits;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.security.AuthoritiesConstants;
import com.bevis.user.UserRepository;
import com.bevis.user.domain.User;
import com.bevis.user.dto.OnUserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bevis.common.util.DateUtil.convertInstantToLocalDate;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class CreditsUpdater {

    private final BigDecimal DEFAULT_FREE_CHARGE = BigDecimal.valueOf(10);//TODO to config

    private final CreditsChargeService creditsChargeService;
    private final CreditsBalanceService creditsBalanceService;
    private final UserRepository userRepository;

    @EventListener
    @Transactional
    public void handleUserRegisteredEvent(OnUserRegisteredEvent e) {
        creditsChargeService.freeChargeUser(e.getUser(), DEFAULT_FREE_CHARGE, "Initial credit balance");
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    void updateCreditsByFreePlan(){
        List<User> users = userRepository.findAll()
                .stream()
                .filter(x-> Objects.equals(AuthoritiesConstants.USER, x.getAuthority()))
                .collect(Collectors.toList());
        Instant now  = Instant.now();
        int currentDayOfMonth = now.atZone(ZoneId.systemDefault()).getDayOfMonth();
        for (User user: users){
            CreditsBalance userBalance = creditsBalanceService.getUserBalance(user);
            if (Objects.nonNull(userBalance)) {
                Instant dateTime = userBalance.getDateTime();
                LocalDate date = convertInstantToLocalDate(dateTime);
                int lastChargeDayOfMonth = date.getDayOfMonth();
                if (!now.equals(dateTime) && Objects.equals(currentDayOfMonth, lastChargeDayOfMonth)) {
                    if (DEFAULT_FREE_CHARGE.compareTo(userBalance.getBalance()) > 0) {
                        BigDecimal amountToCharge = DEFAULT_FREE_CHARGE.subtract(userBalance.getBalance());
                        creditsChargeService.freeChargeUser(user, amountToCharge, "Monthly income charge to user " + user.getEmail());
                    }
                }
            }
        }
    }
}
