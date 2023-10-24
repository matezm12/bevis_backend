package com.bevis.common.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;

public class DateUtilTest {

    @Test
    public void testConvertInstantToDateTimeString() {
        String dateTime = DateUtil.convertInstantToDateTimeString(Instant.now());
        assert dateTime != null;
    }
}
