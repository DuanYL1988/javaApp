package com.util;

public class TextUtil {

    public static boolean isNotEmpty(String text) {
        if (null != text && !"".equals(text)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String arg1, String arg2) {
        return (isNotEmpty(arg1) && isNotEmpty(arg2));
    }

    public static boolean isNotEmpty(String arg1, String arg2, String arg3) {
        return (isNotEmpty(arg1, arg2) && isNotEmpty(arg3));
    }

    public static boolean arrayContains(String value,String[] array) {
        boolean existFlag = false;
        for(String member : array) {
            if (member.equals(value)) {
                return true;
            }
        }
        return existFlag;
    }

}
