package com.bevis.common.util;

import java.util.Base64;

public final class Base64StringUtil {

    public static String encode(String text){
        byte[] encoded = Base64.getEncoder().encode(text.getBytes());
        return new String(encoded);

    }

    public static String decode(String encoded){
        byte[] decoded = Base64.getDecoder().decode(encoded);
        return new String(decoded);
    }
}
