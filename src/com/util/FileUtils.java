package com.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pojo.Field;
import com.pojo.Table;

public class FileUtils {

    /**
     * 取得文件内容
     *
     * @param file
     * @return
     */
    public static List<String> getFileText(File file) {
        List<String> fileTxtList = new ArrayList<String>();
        try {
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while (null != (line = br.readLine())) {
                fileTxtList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileTxtList;
    }

    /**
     * 从DDL中取得FIELD集合信息
     *
     * @param file
     * @return
     */
    public static Table getFieldListFromDDL(File file) {
        Table table = new Table();
        table.setName(file.getName().substring(0,file.getName().indexOf(".")));
        List<Field> fieldList = new ArrayList<Field>();
        Map<String,Field> fieldMap = new HashMap<String, Field>();
        try {
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while (null != (line = br.readLine())) {
                line = StringUtils.removeFirestSpace(line);
                if (");".equals(line)) break;
                if (line.toUpperCase().indexOf("PRIMARY KEY(") >= 0) {
                    String pk = line.substring(line.indexOf("(")-1,line.indexOf(")"));
                    table.setPrimaryKeys(pk);
                }

                Field field = getFieldInfoFromDDL(line);
                if (null != field) {
                    fieldList.add(field);
                    fieldMap.put(field.getDbNm(), field);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        table.setFieldList(fieldList);
        table.setFieldMap(fieldMap);
        return table;
    }

    /**
     * 取得写文件流
     *
     * @param fullpath
     * @return
     */
    public static BufferedWriter getWriter(String fullpath) {
        File writeFile = new File(fullpath);
        BufferedWriter bw = null;
        try {
            if (!writeFile.exists()) {
                writeFile.createNewFile();
            }
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fullpath)),"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bw;
    }

    /**
     * 使用写文件流写入内容
     *
     * @param txt
     * @param bw
     */
    public static void writeFileAndPrintConsole(String txt, BufferedWriter bw) {
        try {
            bw.write(txt + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭流
     *
     * @param bw
     */
    public static void closeWriteSteam(BufferedWriter bw) {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成有内容的文件
     */
    public static void writeFile(String fullpath,String text) {
        BufferedWriter bw = getWriter(fullpath);
        writeFileAndPrintConsole(text, bw);
        closeWriteSteam(bw);
    }

    /**
     * 取得工程路径
     *
     * @return
     */
    public static String getPath() {
        File localFile = new File("");
        return localFile.getAbsolutePath();
    }

    /**
     * 数据库类型与java类型匹配
     */
    public static String changeType(String type, String direct) {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("VARCHAR2", "String");
        mapping.put("VARCHA2", "String");
        mapping.put("CHAR", "String");
        mapping.put("CHARACTER", "String");
        mapping.put("NUMBER", "Integer");
        mapping.put("INT", "Integer");
        mapping.put("NUMERIC", "Integer");
        mapping.put("BIGINT", "Bigdecimal");
        mapping.put("DECIMAL", "Bigdecimal");
        mapping.put("TIMESTAMP", "Date");
        Set<String> keys = mapping.keySet();
        for (String db : keys) {
            String javaNm = mapping.get(db);
            if ("1".equals(direct)) {
                if(db.equals(type)) {
                    return javaNm;
                }
            } else {
                if(javaNm.equals(type)) {
                    return db;
                }
            }
        }
        return "";
    }

    /**
     * Get column information from DDL
     *
     * @param line
     * @return
     */
    private static Field getFieldInfoFromDDL(String line) {
        if (line.toUpperCase().indexOf("CREATE TABLE") >= 0 || line.toUpperCase().indexOf("PRIMARY KEY") >= 0 ) {
            return null;
        } else {
            Field field = new Field();
            String[] lineInfo = line.split(" ");
            String dbNm = lineInfo[0].replaceAll("\"", "");
            field.setDbNm(dbNm);
            field.setJavaNm(changeNm(dbNm, false));
            String type = lineInfo[1];
            // postgreSql
            type = ("character".equals(type)) ? type+" "+lineInfo[2] : type;
            if (type.indexOf("(")>0) {
                field.setSize(Integer.parseInt(type.substring(type.indexOf("(")+1, type.indexOf(")"))));
                type = type.substring(0, type.indexOf("("));
            }
            field.setDbType(type);
            field.setJavaType(changeType(type, "1"));
            // set value
            String loginNm = "";
            if (line.indexOf("--")>0) {
                loginNm = line.substring(line.indexOf("--")+2);
            }
            field.setValue(loginNm);
            field.setLogicNm(loginNm);
            return field;
        }
    }

    /**
     * Change Database column name to java name<br>
     * EXP : CODE_ID ----codeId
     *
     * @param Database column name dbNm
     * @param Java name first char upper flag upFlag
     * @return
     */
    public static String changeNm(String dbNm, boolean upFlag) {
        String javaNm = "";
        // 防止全是小写
        dbNm = dbNm.toUpperCase();
        String[] nms = dbNm.split("_");
        if (nms.length == 1) {
            javaNm = dbNm.toLowerCase();
        } else {
            StringBuilder sb = new StringBuilder(nms[0].toLowerCase());
            for (int i = 1; i < nms.length; i++) {
                String tmp = nms[i];
                sb.append(tmp.substring(0, 1));
                sb.append(tmp.substring(1).toLowerCase());
            }
            javaNm = sb.toString();
        }
        if (upFlag) {
            javaNm = javaNm.substring(0, 1).toUpperCase() + javaNm.substring(1);
        }
        return javaNm.toString();
    }
}
