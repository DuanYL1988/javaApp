package com.application.service;

import java.util.List;

import com.application.dao.ServantDao;
import com.application.model.Servant;

public class ServantService extends MybatisBase {

    /**
     * @param id
     */
    public void getServant(String id) {
        ServantDao dao = super.getSession().getMapper(ServantDao.class);
        Servant rs = dao.selectOneById(id);
        System.out.println(rs.getCreateDatetime());
        super.colseSession();
    }

    /**
     * @param  dto
     * @return
     */
    public List<Servant> getList(Servant dto) {
        ServantDao dao = super.getSession().getMapper(ServantDao.class);
        List<Servant> resultList = dao.selectByDto(dto);
        super.colseSession();
        return resultList;
    }
}
