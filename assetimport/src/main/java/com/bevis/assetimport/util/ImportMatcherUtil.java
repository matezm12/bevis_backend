package com.bevis.assetimport.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImportMatcherUtil {

    public static boolean matchUrl(String s){
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean matchSku(String s) {
        String regex = "^([a-zA-Z0-9]*)[-]([a-zA-Z0-9\\-]*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean matchPublicKey(String s) {
        String regex = "^(((0x).*)|([a-zA-Z0-9]*))[a-zA-Z]([a-zA-Z0-9]*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean matchUpc(String s) {
        String regex = "^[0-9]{12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
