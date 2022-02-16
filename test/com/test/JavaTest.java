package com.test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.application.dto.Table;
import com.application.jdbc.SqliteUtil;
import com.application.service.GetTableInfo;
import com.application.service.ServantService;
import com.application.util.FileUtils;

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
        // OracleUtil oracleClass = new OracleUtil();
        // Connection oracleConn = oracleClass.getConnection();
        // query = "select * from dual ";
        // recoderList = oracleClass.excuteSelectList(query, oracleConn);
        // thisClass.displayResult(recoderList);
    }

    @Test
    public void testReadDDL() {
        GetTableInfo service = new GetTableInfo();
        Table table = service.getTableInfoFromFile("resources\\HERO.ddl");
        System.out.println(table.getColumnList().size());
    }

    @Test
    public void testCreateFolder() {
        FileUtils.createFolder("com\\app\\model", null);
    }

    @Test
    public void testMybatis() {
        ServantService service = new ServantService();
        service.getServant("S194");
    }
}
