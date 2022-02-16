package com.application.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqliteUtil extends BaseConnection implements JdbcConnection {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Connection getConnection() {
        Connection conn = null;
        try {
            //
            Class.forName("org.sqlite.JDBC");
            String dbFilePath = "game.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            logger.info("-> CONNECT TO SQLite");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
