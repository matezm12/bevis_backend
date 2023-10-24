package com.bevis.filecore.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public final class ReflectionUtil {
    public static Object getFieldValue(Object o, String fieldName) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.debug(e.getMessage());
            return null;
        }
    }
}
