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

import com.common.Code;

public class JDBCUtil {

    private static String DRIVE_CLASS = "";
    private static String URL = "";
    private static String USERNAME = "";
    private static String PASSWORD = "";

    private Connection conn;
    private Statement st;

    public JDBCUtil(Properties prop) {
        prop.getProperty(Code.DB_DRIVER);
        DRIVE_CLASS = prop.getProperty(Code.DB_DRIVER);
        URL = prop.getProperty(Code.DB_URL);
        USERNAME = prop.getProperty(Code.DB_USER);
        PASSWORD = prop.getProperty(Code.DB_PASSWORD);

        try {
            Class.forName(DRIVE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String excuteSelectOneCol(String query) {
        ResultSet result = excuteQuery(query);
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
        ResultSet result = excuteQuery(query);
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

    public Boolean excuteInsUpdDel(String sql) {
        getStatement();
        Boolean result = null;
        try {
            System.out.println(sql);
            result = st.execute(sql);
        } catch (SQLException e) {
            System.out.println("-> Excute SQL Error!");
            e.printStackTrace();
        }
        closeConnection();
        return result;
    }

    private ResultSet excuteQuery(String query) {
        getStatement();
        ResultSet result = null;
        try {
            System.out.println(query);
            result = st.executeQuery(query);
            System.out.println("-> Excute SQL SUCCESS!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("-> Excute SQL Error!");
            System.out.println("-> SQL : " + query);
        }
        return result;
    }

    private void getStatement() {
        getConnection();
        try {
            st = conn.createStatement();
            System.out.println("-> Create Statement SUCCESS");
        } catch (SQLException e) {
            System.out.println("-> Create Statement Error!");
        }
    }

    /**
     * Get Database Connection
     */
    private void getConnection() {
        try {
            System.out.println("-> CONNECT TO DATABASE");
            Class.forName(DRIVE_CLASS);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("-> Get Connection Error!");
        } catch (ClassNotFoundException e) {
            System.out.println("-> Load Oracle Driver Error!");
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
            System.out.println("->ResultSet.getColumnName Error!!");
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
