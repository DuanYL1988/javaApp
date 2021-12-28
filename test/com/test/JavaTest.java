package com.test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jdbc.OracleUtil;
import com.jdbc.SqliteUtil;

@RunWith(JUnit4.class)
public class JavaTest {

    @Test
    public void testConnection() throws Exception {
        // 连接Sqlite
        SqliteUtil thisClass = new SqliteUtil();
        Connection sqliteConn = thisClass.getConnection();

        String query = "select * from servant where id = 'S194';";
        List<Map<String, String>> recoderList = thisClass.excuteSelectList(query, sqliteConn);
        thisClass.displayResult(recoderList);
        recoderList = null;

        // 连接Oracle
        OracleUtil oracleClass = new OracleUtil();
        Connection oracleConn = oracleClass.getConnection();
        query = "select * from dual ";
        recoderList = oracleClass.excuteSelectList(query, oracleConn);
        thisClass.displayResult(recoderList);
    }

}
