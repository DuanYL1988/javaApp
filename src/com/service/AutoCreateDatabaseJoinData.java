package com.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.pojo.Field;
import com.pojo.Table;
import com.util.DateTimeUtil;
import com.util.FileTextUtil;
import com.util.FileUtils;
import com.util.JDBCUtil;
import com.util.StringUtils;

/**
 * Test data auto insert or import to csv file
 *
 * @author Duan YL
 */
public class AutoCreateDatabaseJoinData {

    /* config properties file */
    private static final String PROP_FILE = "config.properties";
    /* table join relation info */
    private static final String JOIN_FILE = "tableJoinInfo.txt";

    /* File util */
    FileUtils fileUtil = new FileUtils();
    /* Text util */
    FileTextUtil textUtil = new FileTextUtil();
    /* Text util */
    JDBCUtil util = new JDBCUtil(PROP_FILE);

    /* Properties util */
    Properties prop;
    /* Program location */
    private final String path;
    /* Folder existed ddl file list */
    private final File[] tableDDL;

    /*  */
    private List<String> joinRealtionText = new ArrayList<String>();
    /* */
    private final Map<String,Table> tableMap = new HashMap<String, Table>();
    /* */
    Table preTable = null;

    /**
     * Build method
     */
    public AutoCreateDatabaseJoinData() {
        // Read File To Get Field Info
        path = fileUtil.getPath();
        tableDDL = new File(path+"//ddl").listFiles();
        // Get Properties
        try {
            FileReader reader = new FileReader(path + "//" + PROP_FILE);
            prop = new Properties();
            prop.load(reader);
        } catch (Exception e) {
            System.out.println("PROPERTIES FILE WILL NOT FOUND,CHECK PLEASE!");
            return;
        }
        // Get Table Relation
        joinRealtionText = fileUtil.getFileText(new File(path + "//" + JOIN_FILE));
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
        boolean whereFlag = false;

        for (String textLine : joinRealtionText) {
            // Get Master Table Name
            if (0 == index) {
                // Create Master Table Insert Query
                getDBColInfo(textLine.trim());
            } else if (textLine.indexOf("LEFT JOIN") >= 0) {
                // Create Join table insert query
                getDBColInfo(textLine.split(" ")[2]);
            } else if (textLine.indexOf("ON") >= 0 || textLine.indexOf("AND")>=0) {
                // Get join column value on join part
                setTblLink(textLine);
            } else if (textLine.indexOf("WHERE") >= 0) {
                // after where part get column value without join keywords[AND ON]
                whereFlag = true;
            } else if (whereFlag && textLine.indexOf(" = ") >= 0) {
                // after where part
                setTblLink(textLine);
            } else {
                getDBColInfo(textLine);
            }
            index++;
        }
        doServic();
    }

    /**
     *
     * @param sqlLine
     */
    private void setTblLink(String sqlLine) {
        sqlLine = (sqlLine.indexOf("ON")==0) ? sqlLine.substring(3) : sqlLine;
        sqlLine = (sqlLine.indexOf("AND")==0) ? sqlLine.substring(4) : sqlLine;
        String[] linkpt = sqlLine.split("=");
        String tbl1 = linkpt[0].split("\\.")[0].trim();
        String col1 = linkpt[0].split("\\.")[1].trim();
        Table leftTable = tableMap.get(tbl1);
        // when condition is fixed
        if (linkpt[1].indexOf(".")<0) {
            leftTable.getFieldMap().get(col1).setValue(linkpt[1].trim());
            tableMap.put(tbl1,leftTable);
        } else {
            String value = leftTable.getFieldMap().get(col1).getValue();
            String tbl2 = linkpt[1].split("\\.")[0].trim();
            String col2 = linkpt[1].split("\\.")[1].trim();
            Table rightTable = tableMap.get(tbl2);
            rightTable.getFieldMap().get(col2).setValue(value);
            tableMap.put(tbl2,rightTable);
        }
    }

    private void doServic() {
        String mode = prop.getProperty("OUTPUT", "SQL");
        Set<String> keys = tableMap.keySet();
        for (String key : keys ) {

            if ("SQL".equals(mode)) {
                createInsertQuery(tableMap.get(key));
            } else if ("CSV".equals(mode)) {
                createCsv(tableMap.get(key));
            }
        }
    }
    /**
     * Loop ddl files to get info by table name
     *
     * @param tableNm
     * @return
     */
    private void getDBColInfo(String tableNm) {
        if (null != tableMap.get(tableNm)) {
            return;
        }
        for (File file : tableDDL) {
            if (file.getName().indexOf(".sql") <= 1) {
                continue;
            }
            String tblNm = file.getName().substring(0, file.getName().indexOf("."));
            // While Table DDL existed
            if (tblNm.equals(tableNm)) {
                Table table = fileUtil.getFieldListFromDDL(file);
                // get table put into map
                tableMap.put(tableNm, table);
            }
        }
    }

    /**
     * @param tableNm
     */
    private void createCsv(Table table) {
        String tableNm = table.getName();
        List<Field> fields = table.getFieldList();
        if (null == fields)
            return;
        // Insert count
        int insertCount = Integer.parseInt((String) prop.get(tableNm));
        StringBuilder csv = new StringBuilder();
        for (int i = 0; i <= insertCount; i++) {
            int colIndex = 0;
            for (Field field : fields) {
                String value = table.getFieldMap().get(field.getDbNm()).getValue();
                field.setValue(value);
                if (i == 0) {
                    csv.append("\"" + field.getDbNm() + "\"");
                } else {
                    csv.append(textUtil.setValueByType(field, i , colIndex));
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
    private void createInsertQuery(Table table) {
        // Loop Table DDL Files
        List<Field> fields = table.getFieldList();
        String tableNm = table.getName();
        if (null == fields)
            return;
        String query = insertCreater(tableNm, fields);

        BufferedWriter bw = fileUtil.getWriter(path + "//" + "INSERT_" + tableNm + ".sql");
        fileUtil.writeFileAndPrintConsole(query, bw);
        fileUtil.closeWriteSteam(bw);
    }

    /**
     *
     * INSERT COUNT BY PREPERTY FILE SETTING COUNT
     *
     * @param tableNm
     * @param fields
     * @return
     */
    private String insertCreater(String tableNm, List<Field> fields) {

        // Insert count
        int insertCount = Integer.parseInt((String) prop.get(tableNm));
        // One data column index
        StringBuilder result = new StringBuilder();

        String excute = (String) prop.getOrDefault("EXCUTE_FLAG", "0");
        StringBuilder excuteQuery = new StringBuilder();
        // LOOP INSERT
        for (int currIdx = 1; currIdx <= insertCount; currIdx++) {

            StringBuilder insertColpart = new StringBuilder("INSERT INTO " + tableNm + "(");
            StringBuilder insertValpart = new StringBuilder(") VALUES (");
            int colIndex = 0;
            // LOOP COLUMN
            for (Field field : fields) {
                insertColpart.append(field.getDbNm());

                // Set fixed colunm value
                String value = "";
                if("RECODE_USER_CD".equals(field.getDbNm().toUpperCase())) {
                    // get update user code
                    value = prop.getProperty("USER_CD","Duan Yl");
                } else if ("RECODE_DATE".equals(field.getDbNm().toUpperCase())) {
                    // get update date
                    value = prop.getProperty("UPDATE_DATE","Duan Yl");
                    value = StringUtils.isEmpty(value) ? DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_HMS_POSTGRE) : value;
                } else {
                    // get insert value from field seted
                    value = textUtil.setValueByType(field, currIdx,colIndex);
                }
                insertValpart.append(value);

                if (colIndex < fields.size() - 1) {
                    insertColpart.append(",");
                    insertValpart.append(",");
                }
                colIndex++;
            }
            insertColpart.append(insertValpart.toString() + ");" + "\r\n");
            result.append(insertColpart);
            // insert excute
            if ("1".equals(excute)) {
                excuteQuery.append(insertColpart);
                if (currIdx%100 == 0) {
                    // TODO jdbc

                    excuteQuery = new StringBuilder("");
                }
            }
        }
        return result.toString();
    }

}
