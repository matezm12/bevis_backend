package com.bevis.credits.impl;

import com.bevis.credits.domain.CreditsBalance;
import com.bevis.credits.domain.CreditsCharge;
import com.bevis.credits.domain.CreditsPayment;
import com.bevis.credits.dto.ActionType;
import com.bevis.credits.dto.ChargeCreditsHistoryItem;
import com.bevis.credits.dto.CreditsHistoryItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@Slf4j
class CreditsHistoryMapper {
    public CreditsHistoryItem map(CreditsBalance creditsBalance) {
        Set<CreditsPayment> payments = creditsBalance.getPayments();
        if (!payments.isEmpty()) {
            CreditsHistoryItem historyItem = new CreditsHistoryItem();
            historyItem.setItemType(ActionType.CREDITS_PAYMENT);
            historyItem.setDateTime(creditsBalance.getDateTime());
            historyItem.setBalanceInUnits(creditsBalance.getBalance());
            historyItem.setValueInUnits(payments.stream()
                    .map(CreditsPayment::getCredits).reduce(BigDecimal.ZERO, BigDecimal::add));
            return historyItem;
        } else {
            Set<CreditsCharge> creditsCharges = creditsBalance.getCharges();
            ChargeCreditsHistoryItem chargeCreditsHistoryItem = new ChargeCreditsHistoryItem();
            chargeCreditsHistoryItem.setDateTime(creditsBalance.getDateTime());
            chargeCreditsHistoryItem.setBalanceInUnits(creditsBalance.getBalance());
            if (!creditsCharges.isEmpty()) {
                chargeCreditsHistoryItem.setItemType(ActionType.CREDITS_CHARGE);
                creditsCharges.stream().findFirst().ifPresent(creditsCharge -> {
                    chargeCreditsHistoryItem.setValueInUnits(creditsCharge.getCredits());
                    chargeCreditsHistoryItem.setChargeType(creditsCharge.getSource());
                });
                chargeCreditsHistoryItem.setPurchasePrice(BigDecimal.valueOf(0));//todo
            } else {
                log.warn("Unknown type of balance history action");
                chargeCreditsHistoryItem.setItemType(ActionType.UNKNOWN);

            }
            return chargeCreditsHistoryItem;
        }
    }
}
