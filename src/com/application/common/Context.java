package com.application.common;

public class Context {

    public static final String POINT = ".";

    public static final String SLASH = "\\";

    public static final String SPACE = " ";

    public static final String COMMA = ",";

    public static final String UTF_8 = "UTF-8";

    /* 换行 */
    public static final String CRLF = "\r\n";
    /* 换行 */
    public static final String CRLF2 = "\r\n\r\n";
    /* 2空格 */
    public static final String SPACE2 = "  ";
    /* 4空格 */
    public static final String SPACE4 = "    ";
    /* 6空格 */
    public static final String SPACE6 = "      ";
    /* 8空格 */
    public static final String SPACE8 = "        ";
    /* 10空格 */
    public static final String SPACE10 = "          ";

    /* */
    public static final String TBL_PK = "primaryKey";

    /* */
    public static final String COLUMN_TITLE = "{columnTitle}";
    /* */
    public static final String COLUMN_INFO = "{columnInfo}";

    public static String mkSpace(int count) {
        String space = "";
        for (int i = 0; i < count; i++) {
            space += " ";
        }
        return space;
    }
}
