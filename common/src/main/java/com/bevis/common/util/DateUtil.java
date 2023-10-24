package com.bevis.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateUtil {
    public static LocalDate convertInstantToLocalDate(Instant instant){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return instant.atZone(defaultZoneId).toLocalDate();
    }

    public static String convertInstantToDateString(Instant instant){
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .withLocale( Locale.US )
                        .withZone( ZoneId.systemDefault() );
        return formatter.format(instant);
    }

    public static String convertInstantToDateTimeString(Instant instant){
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.of("UTC+8"));
            return formatter.format(instant);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertInstantToGmtDateTimeString(Instant instant){
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone( ZoneId.of("GMT") );
        return formatter.format(instant) + " GMT";
    }
}
