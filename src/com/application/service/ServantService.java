package com.application.service;

import org.apache.ibatis.session.SqlSession;

import com.application.dao.ServantRepository;
import com.application.model.Servant;

public class ServantService extends MybatisBase {

    ServantRepository dao;

    public ServantService() {
        SqlSession session = super.getSession();
        dao = session.getMapper(ServantRepository.class);
    }

    public void getServant(String id) {
        Servant rs = dao.selectOneById(id);
        System.out.println(rs.getName());
    }
}
