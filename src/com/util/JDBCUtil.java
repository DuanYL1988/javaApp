package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.common.Code;

public class JDBCUtil {

    private static String DRIVE_CLASS = "";
    private static String URL = "";
    private static String USERNAME = "";
    private static String PASSWORD = "";

    private SecurityUtil security;

    public JDBCUtil() {
        security = new SecurityUtil();
        PropertyUtil propUtil = new PropertyUtil();

        DRIVE_CLASS = propUtil.getParamByKey(Code.DB_DRIVER, null,
                Code.MODE_PARAM);
        URL = propUtil.getParamByKey(Code.DB_URL, null, Code.MODE_PARAM);
        USERNAME = propUtil.getParamByKey(Code.DB_USER, null, Code.MODE_PARAM);
        PASSWORD = propUtil.getParamByKey(Code.DB_PASSWORD, null,
                Code.MODE_PARAM);

        try {
            Class.forName(DRIVE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String dbConnection(String website, String account,
            boolean bank_kbn) {
        String resultText = "";
        StringBuffer sql = new StringBuffer(
                "select * from PASSWORD_MANAGE where ");
        // 选卡号加密的情况下，对输入的卡号进行加密验证
        if (bank_kbn && TextUtil.isNotEmpty(account)) {
            account = security.enCrypt(account);
        }
        if (null != website && !"".equals(website)) {
            sql.append("WEBSITE LIKE '%" + website + "%' and ");
        }
        if (null != account && !"".equals(account)) {
            sql.append("USER_NAME = '" + account + "' and ");
        }
        sql.append(" 1 = 1");
        System.out.println(sql.toString());
        ResultSet result = executeQuary(sql.toString());
        try {
            while (result.next()) {
                resultText = resultText + "注册网站:" + result.getString("WEBSITE")
                        + "\n";
                // 未选卡号加密的情况下，不对卡号进行解密
                String db_account = result.getString("USER_NAME");
                if (!bank_kbn) {
                    resultText = resultText + "账户名   :" + db_account + "\n";
                } else {
                    resultText = resultText + "账户名   :"
                            + security.deCrypt(db_account) + "\n";
                }
                String pswd = security.deCrypt(result.getString("USER_PSWD"));
                resultText = resultText + "解密密码:" + pswd + "\n\n";
            }
        } catch (SQLException e) {
            resultText = Code.MSG_SELECT_ERROE;
            e.printStackTrace();
            return resultText;
        }
        return resultText;
    }

    public String updateDb(String websit, String account, String pswd) {
        StringBuffer sql = new StringBuffer(
                "INSERT INTO PASSWORD_MANAGE(USER_NAME,USER_PSWD,WEBSITE) VALUES(");
        sql.append(paramFormat(account) + ",");
        sql.append(paramFormat(pswd) + ",");
        sql.append(paramFormat(websit) + ");");
        System.out.println(sql.toString());
        String resultText = Code.MSG_UPDATE_SUCCESS;
        try {
            insertIntoDB(sql.toString());
            resultText = resultText + "\n 账号:" + account;
            resultText = resultText + "\n 密码:" + pswd;
        } catch (Exception e) {
            e.printStackTrace();
            return Code.MSG_SELECT_ERROE;
        }
        return resultText;
    }

    private ResultSet executeQuary(String sql) {
        ResultSet result = null;
        try {
            Connection conn = getConnection();
            Statement st = conn.createStatement();
            result = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertIntoDB(String sql) {
        Connection conn = getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();
            boolean flg = st.execute(sql);
            System.out.println(flg);
            closeConnection(st, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private void closeConnection(Statement st, Connection conn) {
        try {
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String paramFormat(String param) {
        return "'" + param + "'";
    }

}
