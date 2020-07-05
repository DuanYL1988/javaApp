package com.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exception.LocalException;
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
    JDBCUtil jdbcUtil;

    /* Properties util */
    Properties prop;
    /* Program location */
    private final String path = FileUtils.getPath();
    /* Folder existed ddl file list */
    private final File[] tableDDL = new File(path + "//ddl").listFiles();;
    /*  */
    private List<String> joinRealtionText = new ArrayList<String>();
    /* */
    private final Map<String, Table> tableMap = new HashMap<String, Table>();
    /* */
    Table preTable = null;

    Logger logger = null;

    /**
     * Build method
     */
    public AutoCreateDatabaseJoinData() {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.info("処理開始");
        // Get Properties
        try {
            FileReader reader = new FileReader(path + "//" + PROP_FILE);
            prop = new Properties();
            prop.load(reader);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        jdbcUtil = new JDBCUtil(prop);
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
            } else if (textLine.indexOf("ON") >= 0 || textLine.indexOf("AND") >= 0) {
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
        try {
            doServic();
            logger.info("処理正常終了");
        } catch (LocalException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     *
     * @param sqlLine
     */
    private void setTblLink(String sqlLine) {
        sqlLine = (sqlLine.indexOf("ON") == 0) ? sqlLine.substring(3) : sqlLine;
        sqlLine = (sqlLine.indexOf("AND") == 0) ? sqlLine.substring(4) : sqlLine;
        String[] linkpt = sqlLine.split("=");
        String tbl1 = linkpt[0].split("\\.")[0].trim();
        String col1 = linkpt[0].split("\\.")[1].trim();
        Table leftTable = tableMap.get(tbl1);
        // when condition is fixed
        if (linkpt[1].indexOf(".") < 0) {
            leftTable.getFieldMap().get(col1).setValue(linkpt[1].trim());
            tableMap.put(tbl1, leftTable);
        } else {
            String value = leftTable.getFieldMap().get(col1).getValue();
            String tbl2 = linkpt[1].split("\\.")[0].trim();
            String col2 = linkpt[1].split("\\.")[1].trim();
            Table rightTable = tableMap.get(tbl2);
            rightTable.getFieldMap().get(col2).setValue(value);
            tableMap.put(tbl2, rightTable);
        }
    }

    private void doServic() throws LocalException {
        String mode = prop.getProperty("OUTPUT", "SQL");
        Set<String> keys = tableMap.keySet();
        if ("SQL".equals(mode)) {
            logger.info("Insertファイル出力開始");
            for (String key : keys) {
                createInsertQuery(tableMap.get(key));
            }
            logger.info("Insertファイル出力終了");
        } else if ("CSV".equals(mode)) {
            logger.info("CSVファイル出力開始");
            for (String key : keys) {
                createCsv(tableMap.get(key));
            }
            logger.info("CSVファイル出力終了");
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
                logger.info(tableNm+"テーブルカラム情報取得");
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
                    csv.append(textUtil.setValueByType(field, prop, i, colIndex));
                }
                if (colIndex < fields.size() - 1) {
                    csv.append(",");
                }
                colIndex++;
            }
            csv.append("\r\n");
        }
        BufferedWriter bw = fileUtil.getWriter(path + "//" + "ZZ_CSV" + tableNm + ".csv");
        fileUtil.writeFileAndPrintConsole(csv.toString(), bw);
        fileUtil.closeWriteSteam(bw);
    }

    /**
     * INSERT PROCESS
     *
     * @param tableNm
     * @throws LocalException
     */
    private void createInsertQuery(Table table) throws LocalException {

        // Loop Table DDL Files
        List<Field> fields = table.getFieldList();
        String tableNm = table.getName();
        if (null == fields)
            return;
        String query = insertCreater(tableNm, fields);

        BufferedWriter bw = fileUtil.getWriter(path + "//" + "ZZ_SQL_" + tableNm + ".sql");
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
     * @throws LocalException
     */
    private String insertCreater(String tableNm, List<Field> fields) throws LocalException {

        String excute = (String) prop.getOrDefault("EXCUTE_FLAG", "0");
        checkBefInsert(tableNm,excute);
        // Insert count
        int insertCount = Integer.parseInt((String) prop.get(tableNm));
        // One data column index
        StringBuilder result = new StringBuilder();

        StringBuilder excuteQuery = new StringBuilder();
        // LOOP INSERT
        Date start = new Date();
        for (int currIdx = 1; currIdx <= insertCount; currIdx++) {

            StringBuilder insertColpart = new StringBuilder("INSERT INTO " + tableNm + "(");
            StringBuilder insertValpart = new StringBuilder(") VALUES (");
            int colIndex = 0;
            // LOOP COLUMN
            for (Field field : fields) {
                insertColpart.append(field.getDbNm());

                String value = textUtil.setValueByType(field, prop, currIdx, colIndex);
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
                String txt = prop.getProperty("INSERT_LIMIT");
                int limit = StringUtils.isNotEmpty(txt) ? Integer.parseInt(txt) : 1000;
                if (currIdx % limit == 0) {
                    // jdbc
                    jdbcUtil.excuteInsUpdDel(excuteQuery.toString());
                    excuteQuery = new StringBuilder("");
                }
            }
        }
        // insert remainder
        if (excuteQuery.toString().length() > 0 && "1".equals(excute)) {
            jdbcUtil.excuteInsUpdDel(excuteQuery.toString());
        }
        Date end = new Date();
        logger.info("実行時間"+DateTimeUtil.getUseTime(start, end)+" ms");
        return result.toString();
    }

    private void checkBefInsert(String tableNm,String excute) throws LocalException {

        String updUserCd = prop.getProperty("USER_CD");

        if ("0".equals(excute)) {
            return;
        }
        if (StringUtils.isEmpty(updUserCd)) {
            throw new LocalException("user code not set yet,please ");
        }

        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" RECODE_USER_CD = '" + updUserCd + "'");
        StringBuilder delete = new StringBuilder("DELETE FROM " + tableNm);
        delete.append(where.toString());

        jdbcUtil.excuteInsUpdDel(delete.toString());
    }
}
