package com.peitong.hotfixapp.hotfix.util;

/**
 * Created by peitong.
 */
import java.lang.reflect.Field;

public class ReflectionUtils {
    public ReflectionUtils() {
    }

    public static Object getField(Object obj, Class<?> cl, String field) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static void setField(Object obj, Class<?> cl, String field, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
