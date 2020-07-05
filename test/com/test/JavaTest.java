package com.test;

import java.io.FileReader;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.util.DateTimeUtil;
import com.util.FileUtils;
import com.util.JDBCUtil;

@RunWith(JUnit4.class)
public class JavaTest {

    @Test
    public void testSelectQuery() {
        System.out.println(DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_HMS_POSTGRE));
    }

    @Test
    public void testDelete() throws Exception {
        Properties prop = null;
        FileReader reader = new FileReader(FileUtils.getPath() + "//" + "config.properties");
        prop = new Properties();
        prop.load(reader);

        JDBCUtil jdbcUtil = new JDBCUtil(prop);

        String query = "delete from hero where id > 0";
        jdbcUtil.excuteInsUpdDel(query);
    }


}
