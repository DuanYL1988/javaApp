package com.application.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import com.application.common.Code;

public class PropertyUtil {

    private static String FILE_PATH_PARAM;

    private static String FILE_PATH_OUTPUT;

    private static final String FILE_NAME_PARAM = "config.properties";

    private final FileReadWriteUtil fileUtil = new FileReadWriteUtil();

    Properties prop;

    /**
     * 构造方法
     */
    public PropertyUtil() {
        File file = new File("");
        FILE_PATH_PARAM = file.getAbsolutePath() + "\\" + FILE_NAME_PARAM;
        fileUtil.initFile(FILE_PATH_PARAM);
        prop = getProp(FILE_PATH_PARAM);
    }

    public static void main(String[] args) {
        PropertyUtil util = new PropertyUtil();
        util.checkParamters(util.prop);
    }

    /**
     * 取得所有属性
     */
    private Properties getProp(String path) {
        Properties prop = new Properties();
        try {
            FileReader reader = new FileReader(path);
            prop.load(reader);
            // checkParamters(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * 保存属性
     */
    public String saveDate(String websit, String account, String pswd) {
        try {
            String key = websit + Code.SPILT + account;
            // 先读取配置文件
            FileReader reader = new FileReader(FILE_PATH_OUTPUT);
            Properties prop = new Properties();
            prop.load(reader);
            // 创建输入流
            FileWriter writer = new FileWriter(FILE_PATH_OUTPUT);
            // 新增属性
            prop.setProperty(key, pswd);
            prop.store(writer, "andieguo modify" + new Date().toString());
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return Code.MSG_UPDATE_ERROE;
        }
        String msg = Code.MSG_UPDATE_SUCCESS + "\n";
        msg = msg + "\n" + pswd;
        return msg;
    }

    /**
     * 取得匹配的属性值
     */
    public String getParamByKey(String key) {
        return prop.getProperty(key);
    }

    /**
     * 控制台输出所有属性
     */
    public void checkParamters(Properties prop) {
        Set<Object> keySet = prop.keySet();
        for (Object key : keySet) {
            String KEY = key.toString();
            System.out.println("KEY IS :" + KEY + "; VALUE IS :" + prop.getProperty(KEY));
        }
    }

}
