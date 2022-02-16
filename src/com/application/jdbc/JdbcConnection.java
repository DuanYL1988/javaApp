package com.application.jdbc;

import java.sql.Connection;

/**
 * JDBC接口
 *
 * @author dylsw
 *
 */
public interface JdbcConnection {

    public Connection getConnection();

}
