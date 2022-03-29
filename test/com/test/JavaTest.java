package com.test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.application.creater.DaoCreater;
import com.application.creater.ModelCreater;
import com.application.creater.PageCreater;
import com.application.creater.ServiceCreater;
import com.application.dto.Table;
import com.application.jdbc.MysqlUtil;
import com.application.model.Servant;
import com.application.service.GetTableInfo;
import com.application.service.ServantService;
import com.application.util.FileUtils;

@RunWith(JUnit4.class)
public class JavaTest {

    @Test
    public void testConnection() throws Exception {
        String query = "select * from hero ;";
        // 连接Sqlite
        // SqliteUtil thisClass = new SqliteUtil();
        // Connection sqliteConn = thisClass.getConnection();

        // 连接Oracle
        // OracleUtil oracleClass = new OracleUtil();
        // Connection oracleConn = oracleClass.getConnection();

        // 连接MySql
        MysqlUtil mysql = new MysqlUtil();
        Connection mysqlCon = mysql.getConnection();

        List<Map<String, String>> recoderList = mysql.excuteSelectList(query, mysqlCon);
        mysql.displayResult(recoderList);

    }

    @SuppressWarnings("unused")
    @Test
    public void testReadDDL() {
        //
        boolean hibernateFlag = true;
        String projectName = "SpringBootProject";
        String basePkg = "com.springboot.demo";
        // String projectName = "javaApp";
        // String basePkg = "com.application";
        boolean createFlag = true;
        //
        GetTableInfo service = new GetTableInfo();
        String[] targetArr = new String[] { "SERVANT", "HERO" };
        for (String target : targetArr) {
            String resourceDDL = "resources\\" + target + ".ddl";
            Table table = service.getTableInfoFromFile(resourceDDL);
            String result = "";
            // 控制层Controller
            PageCreater page = new PageCreater();
            page.createController(table, projectName, basePkg, createFlag, hibernateFlag);
            page.createHtml(table, "SpringBootResource", "templates\\", createFlag);
            page.createDetailHtml(table, "SpringBootResource", "templates\\", createFlag);

            // 业务层Service
            ServiceCreater serviceC = new ServiceCreater();
            serviceC.createServiceImpl(table, projectName, basePkg, createFlag, hibernateFlag);
            serviceC.createService(table, projectName, basePkg, createFlag);

            // 持久化层Dao
            DaoCreater daoC = new DaoCreater(table);
            daoC.createMybatis(table, projectName, basePkg, createFlag, hibernateFlag);
            daoC.createMappingXml(table, "MybatisXml", basePkg, createFlag, hibernateFlag);
            if (hibernateFlag) {
                result = daoC.createHibernate(table, projectName, basePkg, createFlag);
            }

            // DB对象Model
            ModelCreater creater = new ModelCreater();
            creater.createModel(table, projectName, basePkg, createFlag, hibernateFlag);
            System.out.println(result);
        }
    }

    @Test
    public void testCreateFolder() {
        FileUtils.createFolder("com\\app\\model", null);
    }

    @Test
    public void testMybatis() {
        ServantService service = new ServantService();
        Servant dto = new Servant();
        dto.setClassType("Saber");
        List<Servant> result = service.getList(dto);
        for (Servant servant : result) {
            System.out.println(servant.getName());
        }
    }
}
