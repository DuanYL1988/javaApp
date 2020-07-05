package com.util;

import java.util.Arrays;
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
    private static final String[] DB_VARCHAR_TYPE = new String[] { "VARCHAR", "VARCHAR2", "CHAR" };
    /* table number type array */
    private static final String[] DB_NUMBER_TYPE = new String[] { "NUMBER", "INT", "DECIMAL", "NUMERIC" };
    /* table number type array */
    private static final String[] DB_TIME_TYPE = new String[] { "DATE", "TIMESTAMP" };

    /**
     *
     * Table column based set value
     *
     * @param Database
     *            column infomation object field
     * @param loop
     *            insert during current index currIdx
     * @return insert value
     */
    public String setValueByType(Field field,Properties prop, int currIdx,int colIndex) {

        String result = "";
        String type = field.getDbType();
        // text
        if (Arrays.asList(DB_VARCHAR_TYPE).contains(type)) {
            String value =field.getValue();
            if("RECODE_USER_CD".equals(field.getDbNm().toUpperCase())) {
                // get update user code
                result = prop.getProperty("USER_CD","Duan Yl");
                return "'" + result + "'";
            } else if ("RECODE_DATE".equals(field.getDbNm().toUpperCase())) {
                // get update date
                result = prop.getProperty("UPDATE_DATE","2020/01/01");
                result = StringUtils.isEmpty(value) ? DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_HMS_POSTGRE) : value;
                return "'" + result + "'";
            } else if (StringUtils.isEmpty(value)) {
                value = setFillText("ITEM**", "*", "0", String.valueOf(colIndex),field.getSize());
                value = value+"_***";
            }
            result = setFillText(value, "*", "0", String.valueOf(currIdx),field.getSize());
            return "'" + result + "'";
        } else if (Arrays.asList(DB_NUMBER_TYPE).contains(type)) {
            return currIdx + "";
        } else if (Arrays.asList(DB_TIME_TYPE).contains(type)) {
            return "CURRENT_TIMESTAMP";
        }
        return "NULL'";
    }

    /**
     *
     * Auto filling from last<br>
     * exp : BCD1****,*,0,999,6 → BCD109
     *
     * @param base
     * @param fmtMark
     * @param repChar
     * @param fillChar
     * @return
     */
    public String setFillText(String base, String fmtMark, String repChar, String fillChar,int maxLength) {
        String result = base;
        // get formt part width
        int width = base.lastIndexOf(fmtMark) - (base.indexOf(fmtMark) - 1);

        if (width > 0 && base.lastIndexOf(fmtMark)>0) {
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
            result = result.substring(0,maxLength);
        }

        return result;
    }

    public static void main(String[] args) {
        FileTextUtil thisclass = new FileTextUtil();
//        String rst = thisclass.setFillText("BCD1****", "*", "0", "999",6);
        String rst = thisclass.setFillText("w", "*", "0", "1",2);
        System.out.println(rst);
    }
}
