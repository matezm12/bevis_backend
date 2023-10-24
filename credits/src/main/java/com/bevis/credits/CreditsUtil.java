package com.bevis.credits;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class CreditsUtil {
    public static String formatUnit(BigDecimal units) {
        return new DecimalFormat("#0.##").format(units);
    }
}
