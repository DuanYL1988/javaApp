package com.common;

public class Code {

    public static final String MSG_SELECT_ERROE = "查找DB时发生失败，请检查DB设置";
    public static final String MSG_SELECT_SUCCESS = "查找成功，结果如下";
    public static final String MSG_SELECT_MARRING = "查无结果";
    public static final String MSG_UPDATE_ERROE = "保存失败";
    public static final String MSG_UPDATE_SUCCESS = "保存成功，加密结果如下";
    public static final String MSG_UPDATE_MARRING = "已经存在！";

    public static final String MSG_VALDATE_WEBSITE = "请输入登陆对象！";
    public static final String MSG_VALDATE_ACCOUNT = "请输入账户信息！";
    public static final String MSG_VALDATE_PSWD = "请输入密码！";
    public static final String MSG_VALDATE_SELECT = "检索模式下\n！";
    public static final String MSG_VALDATE_UPDATE = "更新模式下\n！请输入全部信息";
    public static final String MSG_VALDATE_BANKKBN = "卡号加密模式下\n！";

    public static final String MSG_VALDATE_SUCCESS = "检查通过";

    public static final String SPILT = ";";
    public static final String ENTRY = "\n";
    public static final String TAB = "\t";
    public static final String LOGOUTPUT = "\n DEBUGLOG:";

    /**
     * 取得设置的参数
     */
    public static final String MODE_PARAM = "MODE_PARAM";
    /**
     * 取得本地保存的数据
     */
    public static final String MODE_DATE = "MODE_DATE";

    public static final String CHARSET_CODE_UTF8 = "UTF-8";

    public static final String BANK_KBN = "BANKKBN";
    public static final String SAVE_KBN = "SAVEKBN";

    public static final String DB_URL = "jdbcUrl";
    public static final String DB_DRIVER = "driverClass";
    public static final String DB_USER = "user";
    public static final String DB_PASSWORD = "password";

    public static final String VERSION = "version";

    public static final String MSG_KEY = "MSG_KEY";
    public static final String RESULT = "RESULT";
    public static final String MSG_NO_NEXT = "no next";

    public static final String DRIVER_NAME_RELAX = "雷克斯";
    public static final String DRIVER_NAME_NIA = "尼亚";
    public static final String DRIVER_NAME_TIGER = "虎";
    public static final String DRIVER_NAME_MELAF = "梅勒芙";
    public static final String DRIVER_NAME_CIGE = "齐格";

    public static final String BLADE_ATTR_FIRE = "Fire";
    public static final String BLADE_ATTR_WATER = "Water";
    public static final String BLADE_ATTR_WIND = "Wind";
    public static final String BLADE_ATTR_ICE = "Ice";
    public static final String BLADE_ATTR_LIGHT = "Light";
    public static final String BLADE_ATTR_DARK = "Dark";
    public static final String BLADE_ATTR_EARTH = "Earth";
    public static final String BLADE_ATTR_THUNDER = "Thunder";

    public static final String[] COMBO_01 = new String[] { BLADE_ATTR_FIRE, BLADE_ATTR_FIRE, BLADE_ATTR_FIRE };
    public static final String[] COMBO_02 = new String[] { BLADE_ATTR_FIRE, BLADE_ATTR_FIRE, BLADE_ATTR_LIGHT };
    public static final String[] COMBO_03 = new String[] { BLADE_ATTR_FIRE, BLADE_ATTR_WATER, BLADE_ATTR_ICE };
    public static final String[] COMBO_04 = new String[] { BLADE_ATTR_FIRE, BLADE_ATTR_WATER, BLADE_ATTR_FIRE };

    public static final String[] COMBO_05 = new String[] { BLADE_ATTR_WIND, BLADE_ATTR_WIND, BLADE_ATTR_EARTH };
    public static final String[] COMBO_06 = new String[] { BLADE_ATTR_WIND, BLADE_ATTR_WIND, BLADE_ATTR_THUNDER };
    public static final String[] COMBO_07 = new String[] { BLADE_ATTR_WIND, BLADE_ATTR_ICE, BLADE_ATTR_ICE };

    public static final String[] COMBO_08 = new String[] { BLADE_ATTR_WATER, BLADE_ATTR_WATER, BLADE_ATTR_WATER };
    public static final String[] COMBO_09 = new String[] { BLADE_ATTR_WATER, BLADE_ATTR_WATER, BLADE_ATTR_DARK };
    public static final String[] COMBO_10 = new String[] { BLADE_ATTR_WATER, BLADE_ATTR_EARTH, BLADE_ATTR_WIND };

    public static final String[] COMBO_11 = new String[] { BLADE_ATTR_ICE, BLADE_ATTR_WATER, BLADE_ATTR_WIND };
    public static final String[] COMBO_12 = new String[] { BLADE_ATTR_ICE, BLADE_ATTR_ICE, BLADE_ATTR_DARK };
    public static final String[] COMBO_13 = new String[] { BLADE_ATTR_ICE, BLADE_ATTR_ICE, BLADE_ATTR_EARTH };

    public static final String[] COMBO_14 = new String[] { BLADE_ATTR_EARTH, BLADE_ATTR_FIRE, BLADE_ATTR_WIND };
    public static final String[] COMBO_15 = new String[] { BLADE_ATTR_EARTH, BLADE_ATTR_FIRE, BLADE_ATTR_EARTH };
    public static final String[] COMBO_16 = new String[] { BLADE_ATTR_EARTH, BLADE_ATTR_EARTH, BLADE_ATTR_THUNDER };

    public static final String[] COMBO_17 = new String[] { BLADE_ATTR_DARK, BLADE_ATTR_LIGHT, BLADE_ATTR_THUNDER };
    public static final String[] COMBO_18 = new String[] { BLADE_ATTR_DARK, BLADE_ATTR_DARK, BLADE_ATTR_EARTH };
    public static final String[] COMBO_19 = new String[] { BLADE_ATTR_DARK, BLADE_ATTR_DARK, BLADE_ATTR_DARK };

    public static final String[] COMBO_20 = new String[] { BLADE_ATTR_THUNDER, BLADE_ATTR_FIRE, BLADE_ATTR_WIND };
    public static final String[] COMBO_21 = new String[] { BLADE_ATTR_THUNDER, BLADE_ATTR_FIRE, BLADE_ATTR_ICE };
    public static final String[] COMBO_22 = new String[] { BLADE_ATTR_THUNDER, BLADE_ATTR_THUNDER, BLADE_ATTR_WATER };

    public static final String[] COMBO_23 = new String[] { BLADE_ATTR_LIGHT, BLADE_ATTR_THUNDER, BLADE_ATTR_FIRE };
    public static final String[] COMBO_24 = new String[] { BLADE_ATTR_LIGHT, BLADE_ATTR_FIRE, BLADE_ATTR_WATER };
    public static final String[] COMBO_25 = new String[] { BLADE_ATTR_LIGHT, BLADE_ATTR_LIGHT, BLADE_ATTR_LIGHT };

    public static final String[][] ALL_COMBO = new String[][] { Code.COMBO_01, Code.COMBO_02, Code.COMBO_03, Code.COMBO_04, Code.COMBO_05, Code.COMBO_06, Code.COMBO_07, Code.COMBO_08, Code.COMBO_09,
            Code.COMBO_10, Code.COMBO_11, Code.COMBO_12, Code.COMBO_13, Code.COMBO_14, Code.COMBO_15, Code.COMBO_16, Code.COMBO_17, Code.COMBO_18, Code.COMBO_19, Code.COMBO_20, Code.COMBO_21,
            Code.COMBO_22, Code.COMBO_23, Code.COMBO_24, Code.COMBO_25 };

    public static final String[] ALL_ATTR = new String[] { BLADE_ATTR_FIRE, BLADE_ATTR_LIGHT, BLADE_ATTR_WATER, BLADE_ATTR_THUNDER, BLADE_ATTR_WIND, BLADE_ATTR_ICE, BLADE_ATTR_DARK,
            BLADE_ATTR_EARTH };

    public static String getComboCd(String[] combo, int index) {
        return combo[index].split(":")[0];
    }

    public static String getComboAttr(String[] combo, int index) {
        return combo[index].split(":")[1];
    }

}
