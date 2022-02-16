package com.application.util;

public class TextUtil {

    public static boolean isNotEmpty(String text) {
        if (null != text && !"".equals(text)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInclude(String[] aStrArr, String target) {
        for (String name : aStrArr) {
            if (name.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(String arg1, String arg2) {
        return (isNotEmpty(arg1) && isNotEmpty(arg2));
    }

    public static boolean isNotEmpty(String arg1, String arg2, String arg3) {
        return (isNotEmpty(arg1, arg2) && isNotEmpty(arg3));
    }

    public static boolean arrayContains(String value, String[] array) {
        boolean existFlag = false;
        for (String member : array) {
            if (member.toUpperCase().equals(value)) {
                return true;
            }
        }
        return existFlag;
    }

    /**
     * 将DB的字段名转为Java的驼峰命名
     *
     * @param dbNm        DB字段名
     * @param firstUpFlag 首字母大写
     * @return
     */
    public static String transNmDbToJava(String dbNm, boolean firstUpFlag) {
        String[] nameArr = dbNm.split("_");
        String javaNm = "";
        for (int i = 0; i < nameArr.length; i++) {
            String nm = nameArr[i].toLowerCase();
            if (i > 0 || firstUpFlag) {
                nm = nm.substring(0, 1).toUpperCase() + nm.substring(1).toLowerCase();
            }
            javaNm += nm;
        }
        return javaNm;
    }

}
