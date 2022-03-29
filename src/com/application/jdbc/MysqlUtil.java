package com.application.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MysqlUtil extends BaseConnection implements JdbcConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/duanyl";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVE_CLASS = "com.mysql.cj.jdbc.Driver";
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Connection getConnection() {
        Connection conn = null;
        try {
            logger.info("-> CONNECT TO MYSQL DATABASE");
            Class.forName(DRIVE_CLASS);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            logger.error("-> Get Connection Error!");
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error("-> Load Driver Error!");
        }
        return conn;
    }

}
