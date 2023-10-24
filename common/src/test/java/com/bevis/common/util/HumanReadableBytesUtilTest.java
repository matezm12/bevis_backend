package com.bevis.common.util;


import org.junit.jupiter.api.Test;

public class HumanReadableBytesUtilTest {

    @Test
    public void testHumanReadableByteCountSI() {
        String s = HumanReadableBytesUtil.humanReadableByteCount(122600);
        assert s != null;
    }

    @Test
    public void testHumanReadableByteCountBin() {
        String s = HumanReadableBytesUtil.humanReadableByteCountBin(122600);
        assert s != null;
    }
}
