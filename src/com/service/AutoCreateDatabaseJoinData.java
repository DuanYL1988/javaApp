package com.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.pojo.Field;
import com.pojo.Table;
import com.util.FileUtils;

public class AutoCreateDatabaseJoinData {

    /* config properties file */
    private static final String PROP_FILE = "config.properties";
    /* table join relation info */
    private static final String JOIN_FILE = "tableJoinInfo.txt";
    /* table text type array */
    private static final String[] DB_VARCHAR_TYPE = new String[] {"VARCHAR","VARCHAR2","CHAR"};
    /* table number type array */
    private static final String[] DB_NUMBER_TYPE = new String[] {"NUMBER","INT","DECIMAL","NUMERIC"};
    /* table number type array */
    private static final String[] DB_TIME_TYPE = new String[] {"DATE","TIMESTAMP"};

    FileUtils fileUtil = new FileUtils();

    private final String path;
    private final File[] tableDDL;
    Properties prop;
    private List<String> joinInfo = new ArrayList<String>();

    private Table mainTable = null;

    public AutoCreateDatabaseJoinData() {
        // Read File To Get Field Info
        path = fileUtil.getPath();
        tableDDL = new File(path).listFiles();

        // Get Properties
        FileReader reader;
        try {
            reader = new FileReader(path+"//"+PROP_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("PROPERTIES FILE WILL NOT FOUND,CHECK PLEASE!");
            return;
        }
        prop = new Properties();
        try {
            prop.load(reader);
        } catch (IOException e) {
            System.out.println("PROPERTIES FILE ERROR");
            return;
        }

        // Get Table Relation
        joinInfo = fileUtil.getFileText(new File(path+"//"+JOIN_FILE));
    }

    public static void main(String[] args) {
        AutoCreateDatabaseJoinData dataCreater = new AutoCreateDatabaseJoinData();

        dataCreater.analyzeRelation();
    }

    /**
     * GET SETTING RELATION
     */
    private void analyzeRelation() {
        int index = 0;
        Table joinTbl = null;
        boolean whereFlag = false;
        List<String> finishTblList = new ArrayList<String>();
        for(String textLine : joinInfo) {

            // Get Master Table Name
            if (0==index && null == mainTable) {
                // Initilize Table Object
                mainTable = new Table();
                List<Field> joinColunm = new ArrayList<Field>();
                mainTable.setName(textLine.trim());
                mainTable.setJoinField(joinColunm);

                // Create Master Table Insert Query
                checkDeal(finishTblList, textLine.trim());

            } else if (textLine.indexOf("LEFT JOIN")>=0) {
                joinTbl = new Table();
                joinTbl.setName(textLine.split(" ")[2]);

                // Create Join table insert query
                checkDeal(finishTblList,textLine.split(" ")[2]);
            } else if(textLine.indexOf(" ON ")>=0) {
                // TODO
            } else if(textLine.indexOf("WHERE")>=0) {

                whereFlag = true;
            } else if(whereFlag && textLine.indexOf(" = ")>=0) {
                String innerJoinInfo = textLine.split("=")[1];
                if (innerJoinInfo.indexOf(".")>=0) {
                    String targetTbl = innerJoinInfo.substring(0,innerJoinInfo.indexOf(".")).trim();
                    checkDeal(finishTblList, targetTbl);
                }
            }

            index ++;
        }
    }

    private void checkDeal(List<String> finishTblList,String tableNm) {
        if(finishTblList.contains(tableNm)) {
            return;
        }
        String mode = prop.getProperty("OUTPUT", "SQL");
        String excute = (String) prop.get("EXCUTE_FLAG");
        if ("SQL".equals(mode)) {
            createInsertQuery(tableNm);
            if ("1".equals(excute)) {
                // TODO POSTGRE DRIVER
            }

        } else if ("CSV".equals(mode)) {
            createCsv(tableNm);
        }
        finishTblList.add(tableNm);
    }

    /**
     * @param tableNm
     */
    private void createCsv(String tableNm) {
        List<Field> fields = getDBColInfo(tableNm);
        if (null== fields) return;
     // Insert count
        int insertCount = Integer.parseInt((String)prop.get(tableNm));
        StringBuilder csv = new StringBuilder();
        for(int i=0;i<=insertCount;i++) {
            int colIndex = 0;
            for (Field field : fields) {
                if (i==0) {
                    csv.append("\""+field.getDbNm()+"\"");
                } else {
                    csv.append(setValueByType(field,i));
                }
                if (colIndex < fields.size() - 1) {
                    csv.append(",");
                }
                colIndex++;
            }
            csv.append("\r\n");
        }
        BufferedWriter bw = fileUtil.getWriter(path + "//" + "INSERT_" + tableNm + ".csv");
        fileUtil.writeFileAndPrintConsole(csv.toString(), bw);
        fileUtil.closeWriteSteam(bw);
    }
    /**
     * INSERT PROCESS
     *
     * @param tableNm
     */
    private void createInsertQuery(String tableNm) {
        // Loop Table DDL Files
        List<Field> fields = getDBColInfo(tableNm);
        if (null== fields) return;
        String query = insertCreater(tableNm, fields);
        BufferedWriter bw = fileUtil.getWriter(path + "//" + "INSERT_" + tableNm + ".sql");
        fileUtil.writeFileAndPrintConsole(query, bw);
        fileUtil.closeWriteSteam(bw);
    }

    private List<Field> getDBColInfo(String tableNm) {
        for (File file : tableDDL) {
            if (file.getName().indexOf(".sql")<=1) {
                continue;
            }
            String tblNm = file.getName().substring(0,file.getName().indexOf("."));
            // While Table DDL existed
            if (tblNm.equals(tableNm)) {
                List<Field> fields = fileUtil.getFieldListFromDDL(file);
                return fields;
            }
        }
        return null;
    }

    /**
     *
     * INSERT COUNT BY PREPERTY FILE SETTING COUNT
     *
     * @param tableNm
     * @param fields
     * @return
     */
    private String insertCreater(String tableNm,List<Field> fields){

        // Insert count
        int insertCount = Integer.parseInt((String)prop.get(tableNm));
        // One data column index
        StringBuilder result = new StringBuilder();
        // LOOP INSERT
        for (int currIdx = 1; currIdx <= insertCount; currIdx++) {

            StringBuilder insertColpart = new StringBuilder("INSERT INTO "+tableNm+"(");
            StringBuilder insertValpart = new StringBuilder(") VALUES (");
            int colIndex = 0;
            // LOOP COLUMN
            for (Field field : fields) {
                insertColpart.append(field.getDbNm());
                insertValpart.append(setValueByType(field,currIdx));

                if (colIndex < fields.size() - 1) {
                    insertColpart.append(",");
                    insertValpart.append(",");
                }
                colIndex++;
            }
            insertColpart.append(insertValpart.toString()+");"+"\r\n");
            result.append(insertColpart);
        }
        return result.toString();
    }

    /**
     *
     * Table column based set value
     *
     * @param DATABASE FIELD INFOMATION field
     * @param INSET COUNT'S CUNNENT COUNT currIdx
     * @return value
     */
    private String setValueByType(Field field,int currIdx) {
        String result = "";
        String type = field.getDbType();
        int accuracy = null == field.getSize() ? 0:field.getSize();
        // text
        if (Arrays.asList(DB_VARCHAR_TYPE).contains(type)) {
            String prefix = (null == field.getPrefixValue()) ? "":field.getPrefixValue();
            result = prefix.replace("#{replace}#", currIdx+"");
            if ("0".equals(result.substring(0,1))) {
                int indLength = String.valueOf(currIdx).length();
                int cut = result.length()-indLength;
                if (cut<0) {
                    result = String.valueOf(currIdx).substring(indLength-accuracy);
                } else {
                    result = result.substring(0, cut)+currIdx;
                }
            };
            return "'"+result+"'";
        } else if (Arrays.asList(DB_NUMBER_TYPE).contains(type)) {
            return currIdx+"";
        } else if (Arrays.asList(DB_TIME_TYPE).contains(type)) {
            return"CURRENT_TIMESTAMP";
        }
        return "NULL";
    }
}
