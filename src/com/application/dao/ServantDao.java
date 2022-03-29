package com.application.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.application.model.Servant;

public interface ServantDao {

    Servant selectOneById(@Param("id") String id);

    Servant selectOneByUniqueKey(@Param("imgName") String imgName);

    List<Servant> selectByDto(Servant servant);

    List<Servant> customQuary(Servant servant);

    void insert(Servant servant);

    void update(Servant servant);

}
