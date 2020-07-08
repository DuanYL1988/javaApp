package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Code;
import com.exception.LocalException;

public class JDBCUtil {

    private static String DRIVE_CLASS = "";
    private static String URL = "";
    private static String USERNAME = "";
    private static String PASSWORD = "";

    private Connection conn;
    private Statement st;

    Logger logger;
    public JDBCUtil(Properties prop) {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.info("データベース接続情報取得");
        prop.getProperty(Code.DB_DRIVER);
        DRIVE_CLASS = prop.getProperty(Code.DB_DRIVER);
        URL = prop.getProperty(Code.DB_URL);
        USERNAME = prop.getProperty(Code.DB_USER);
        PASSWORD = prop.getProperty(Code.DB_PASSWORD);

        try {
            Class.forName(DRIVE_CLASS);
        } catch (Exception e) {
            logger.error("データベース接続失敗");
            e.printStackTrace();
        }
    }

    public String excuteSelectOneCol(String query) {
        ResultSet result;
        try {
            result = excuteQuery(query);
        } catch (LocalException e1) {
            return null;
        }
        String rst = "";
        try {
            while (result.next()) {
                rst = result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return rst;
    }

    /**
     * Get result list
     */
    public List<Map<String, String>> excuteSelectList(String query) {
        ResultSet result=null;
        try {
            result = excuteQuery(query);
        } catch (LocalException e1) {
            return null;
        }
        String[] cols = getColumns(result);
        List<Map<String, String>> rst = new ArrayList<Map<String, String>>();
        try {
            while (result.next()) {
                Map<String, String> values = new HashMap<String, String>();
                for (int i = 1; i <= cols.length; i++) {
                    // 字段属性
                    String key = result.getMetaData().getColumnName(i);
                    String val = "";
                    // 取得字段值
                    val = result.getString(i);
                    // 控制台输出结果
                    System.out.print(val + Code.TAB);
                    // Map<数据库字段名,单元格值>
                    values.put(key, val);
                }
                // 换行
                System.out.println();
                // [<col1,val>,<col2,val>]这样存放检索出的数据
                rst.add(values);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return rst;
    }

    public Boolean excuteInsUpdDel(String sql) throws LocalException {
        getStatement();
        Boolean result = null;
        try {
            logger.info("-> SQL : "+sql);
            result = st.execute(sql);
        } catch (SQLException e) {
            String msg = StringUtils.createErrorMsg(this.getClass().toString(), "excuteInsUpdDel", "SQL", e.getMessage());
            logger.error(msg);
            throw new LocalException();
        }
        closeConnection();
        return result;
    }

    private ResultSet excuteQuery(String query) throws LocalException {
        getStatement();
        ResultSet result = null;
        try {
            logger.info("-> SQL : " + query);
            result = st.executeQuery(query);
            logger.info("-> Excute SQL SUCCESS");
        } catch (SQLException e) {
            String msg = StringUtils.createErrorMsg(this.getClass().toString(), "excuteQuery", "SQL", e.getMessage());
            logger.error(msg);
            throw new LocalException();
        }
        return result;
    }

    private void getStatement() {
        getConnection();
        try {
            st = conn.createStatement();
            logger.info("-> Create Statement SUCCESS");
        } catch (SQLException e) {
            logger.error("-> Create Statement Error!");
        }
    }

    /**
     * Get Database Connection
     */
    private void getConnection() {
        try {
            logger.info("-> CONNECT TO DATABASE");
            Class.forName(DRIVE_CLASS);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            logger.error("-> Get Connection Error!");
        } catch (ClassNotFoundException e) {
            logger.error("-> Load Driver Error!");
        }
    }

    /**
     * Close DB Connection
     */
    private void closeConnection() {
        try {
            if (null != st) {
                st.close();
            }
            if (null != conn) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Select Columns
     *
     * @param query
     * @return
     */
    private String[] getColumns(ResultSet result) {
        String[] cols = null;
        try {
            // 取得SQL结果列数
            int colCount = result.getMetaData().getColumnCount();
            cols = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                // 取得列名
                cols[i] = result.getMetaData().getColumnName(i + 1);
            }
        } catch (SQLException e) {
            logger.error("->ResultSet.getColumnName Error!!");
        }
        if (null!=cols) {
            for (String col : cols) {
                System.out.print(col + Code.TAB);
            }
        }
        System.out.println("\r" + "---------------------------------------------------");
        return cols;
    }

}
