package com.util;

import com.common.Code;

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

    public static void debugLog(StringBuffer log, String param, String text) {
        log.append(Code.LOGOUTPUT + param + ":" + text);
    }

    public static String debugLog(String value, String param, String text) {
        if (null != param) {
            value = value + Code.LOGOUTPUT + param + ":" + text;
        } else {
            value = value + Code.LOGOUTPUT + text;
        }
        return value;
    }

}
