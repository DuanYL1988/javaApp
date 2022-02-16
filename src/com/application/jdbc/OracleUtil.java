package com.application.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleUtil extends BaseConnection implements JdbcConnection {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String USERNAME = "fireemblem";
    private static final String PASSWORD = "fireemblem";
    private static final String DRIVE_CLASS = "oracle.jdbc.driver.OracleDriver";
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Connection getConnection() {
        Connection conn = null;
        try {
            logger.info("-> CONNECT TO ORACLE DATABASE");
            Class.forName(DRIVE_CLASS);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            logger.error("-> Get Connection Error!");
        } catch (ClassNotFoundException e) {
            logger.error("-> Load Driver Error!");
        }
        return conn;
    }

}
