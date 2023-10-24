package com.bevis.common.util;

import com.google.gson.Gson;

public final class SerializerUtil {
    public static  String serializeObjectToJson(Object o){
        return new Gson().toJson(o);
    }

}
