package com.util;

import java.util.Scanner;

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

    /**
     * Get input from scanner
     *
     * @return scanner input value
     */
    public static String getScannerInput() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        String input="";
        while (true){
            input = scanner.nextLine();
            if (input.equals("")) {
                break;
            }
        };
        return input;
    }

    /**
     *
     * edit error message
     *
     * @param classNm
     * @param methodNm
     * @param type
     * @param info
     * @return
     */
    public static String createErrorMsg(String classNm,String methodNm,String type,String info) {
        StringBuilder errorInfo = new StringBuilder("\r\n");
        errorInfo.append("========================================="+"\r\n");
        errorInfo.append("= class     : "+classNm+"\r\n");
        errorInfo.append("= method  : "+methodNm+"\r\n");
        errorInfo.append("= type       : "+type+"\r\n");
        errorInfo.append("= info       : "+info+"\r\n");
        errorInfo.append("=========================================");
        return errorInfo.toString();
    }
}
