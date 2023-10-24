package com.bevis.assetimport.util;

import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertNotNull;


@Ignore
public class DateConverterTest {

    @Test
    public void convertReaderDateToInstant() {
        String date = "2019-11-21 14:48:26";
        Instant instant = DateConverter.convertReaderDateToInstant(date);
        assertNotNull(instant);
    }
}
