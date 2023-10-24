package com.bevis.common.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

public final class HexConverterUtil {
    public static String convertHexToString(String hex) throws DecoderException {
        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
