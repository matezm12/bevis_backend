package com.bevis.common.util;

public final class HumanReadableBytesUtil {

    //default
    public static String humanReadableByteCount(long bytes) {
        return humanReadableByteCountBin(bytes);
    }

    public static String humanReadableByteCountSI(long bytes) {
        String s = bytes < 0 ? "-" : "";
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1000L ? bytes + " B"
                : b < 999_950L ? String.format("%s%.2f kB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.2f MB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.2f GB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.2f TB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.2f PB", s, b / 1e3)
                : String.format("%s%.2f EB", s, b / 1e6);
    }

    public static String humanReadableByteCountBin(long bytes) {
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1024L ? bytes + " B"
                : b <= 0xfffccccccccccccL >> 40 ? String.format("%.2f KiB", bytes / 0x1p10)
                : b <= 0xfffccccccccccccL >> 30 ? String.format("%.2f MiB", bytes / 0x1p20)
                : b <= 0xfffccccccccccccL >> 20 ? String.format("%.2f GiB", bytes / 0x1p30)
                : b <= 0xfffccccccccccccL >> 10 ? String.format("%.2f TiB", bytes / 0x1p40)
                : b <= 0xfffccccccccccccL ? String.format("%.2f PiB", (bytes >> 10) / 0x1p40)
                : String.format("%.2f EiB", (bytes >> 20) / 0x1p40);
    }
}
