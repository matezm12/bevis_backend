package com.bevis.balance.util;

import java.text.DecimalFormat;
import java.util.Objects;

public final class FiatCurrencyUtil {
    public static Double formatFiatCurrencyBalance(Double fiatBalance) {
        if (Objects.nonNull(fiatBalance)) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return Double.parseDouble(decimalFormat.format(fiatBalance));
        }
        return null;
    }
}
