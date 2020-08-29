package com.util;

import java.util.Properties;

import com.pojo.Field;

/**
 * Various file output process util
 *
 * @author dylsw
 *
 */
public class FileTextUtil {

    /* table text type array */
    private static final String[] DB_VARCHAR_TYPE = new String[] { "VARCHAR", "VARCHAR2", "CHAR", "CHARACTER VARYING" };
    /* table number type array */
    private static final String[] DB_NUMBER_TYPE = new String[] { "NUMBER", "INT", "BIGINT", "DECIMAL", "NUMERIC" };
    /* table number type array */
    private static final String[] DB_TIME_TYPE = new String[] { "DATE", "TIMESTAMP" };

    /**
     *
     * Table column based set value
     *
     * @param Database column infomation object field
     * @param loop     insert during current index currIdx
     * @return insert value
     */
    public static String setValueByType(Field field, Properties prop, int currIdx, int colIndex) {

        String result = "";
        String type = field.getDbType().toUpperCase();
        String value = field.getValue();

        value = getValueFromArray(value, currIdx);

        if (TextUtil.arrayContains(type, DB_VARCHAR_TYPE)) {
            String dbNm = field.getDbNm().toUpperCase();
            if ("null".equals(value.toLowerCase())) {
                return "NULL";
            } else if (dbNm.indexOf("RECODE_USER_CD") >= 0) {
                // get update user code
                result = "TESTER";
                return "'" + result + "'";
            } else if (dbNm.indexOf("INSERT_DATE") >= 0 || field.getDbNm().toUpperCase().indexOf("RECORD_DATE") >= 0) {
                // get update date
                result = prop.getProperty("UPDATE_DATE", "2020/01/01");
                result = StringUtils.isEmpty(value) ? DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_HMS_POSTGRE) : value;
                return "'" + result + "'";
            } else if (dbNm.indexOf("START_DATE") >= 0) {
                value = StringUtils.isEmpty(value) ? "2010/01/01|00:00:00" : value;
                return "'" + value + "'";

            } else if (dbNm.indexOf("END_DATE") >= 0) {
                value = StringUtils.isEmpty(value) ? "2999/01/01|00:00:00" : value;
                return "'" + value + "'";

            } else if (dbNm.indexOf("_DATE") >= 0) {
                value = StringUtils.isEmpty(value) ? "2020/01/01|00:00:00" : value;
                return "'" + value + "'";
            } else if (StringUtils.isEmpty(value)) {
                value = setFillText("ITEM**", "*", "0", String.valueOf(colIndex), field.getSize());
                value = value + "_***";
            }
            if (value.toLowerCase().indexOf("to_char(") >= 0) {
                value = value.replace("*", currIdx + "");
                return value;
            }
            result = setFillText(value, "*", "0", String.valueOf(currIdx), field.getSize());
            // database function
            return "'" + result + "'";
        } else if (TextUtil.arrayContains(type, DB_NUMBER_TYPE)) {
            return StringUtils.isEmpty(value) ? String.valueOf(currIdx)
                    : String.valueOf(Integer.parseInt(value) + currIdx);
        } else if (TextUtil.arrayContains(type, DB_TIME_TYPE)) {
            return "CURRENT_TIMESTAMP";
        }
        return "NULL";
    }

    /**
     *
     * Auto filling from last<br>
     * EXP : BCD1****,*,0,999,6 → BCD109
     *
     * @param base
     * @param fmtMark
     * @param repChar
     * @param fillChar
     * @return
     */
    public static String setFillText(String base, String fmtMark, String repChar, String fillChar, int maxLength) {
        String result = base;
        // get formt part width
        int width = base.lastIndexOf(fmtMark) - (base.indexOf(fmtMark) - 1);

        if (width > 0 && base.lastIndexOf(fmtMark) > 0) {
            // get prefix
            String prefix = base.substring(0, base.indexOf(fmtMark));
            // when fillchar length greater to format part's length
            if (fillChar.length() > width) {
                fillChar = fillChar.substring(fillChar.length() - width);
            }
            // format parts end to start fill new char
            String fmtPart = base.substring(base.indexOf(fmtMark));
            fmtPart = fmtPart.substring(0, width - fillChar.length()) + fillChar;
            // format mark replaceing
            result = prefix + fmtPart.replace(fmtMark, repChar);
        }

        if (result.length() > maxLength) {
            result = result.substring(0, maxLength);
        }

        return result;
    }

    private static String getValueFromArray(String textVal, int currentIndex) {
        String[] arrayVal = textVal.split(",");
        int index = getIndex(arrayVal.length, currentIndex);
        return arrayVal[index];
    }

    private static int getIndex(int max, int current) {
        if (current >= max) {
            current = getIndex(max, current - max);
        }
        return current;
    }

    public static void main(String[] args) {
        String value = "99001,99002";
        for (int i = 0; i < 10; i++) {
            System.out.println(getValueFromArray(value, i));
        }
    }
}
