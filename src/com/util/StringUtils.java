package com.util;

public class StringUtils {

    public static boolean isEmpty(String value) {
        if (null == value || "".equals(value)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String value) {
        if (null == value || "".equals(value)) {
            return false;
        }
        return true;
    }
}
