package com.bevis.assetimport.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public final class DateConverter {
    public static Instant convertReaderDateToInstant(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
        TemporalAccessor temporalAccessor = formatter.parse(date);
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return Instant.from(zonedDateTime);
    }
}
