package com.application.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisBase {

    public SqlSession getSession() {
        SqlSession session = null;
        String resource = "mybatis-config.xml";
        InputStream ins;
        try {
            ins = Resources.getResourceAsStream(resource);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(ins);
            session = sessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return session;
    }
}
