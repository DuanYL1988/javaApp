package com.application.dao;

import com.application.model.Servant;

public interface ServantRepository {

    public Servant selectOneById(String id);

}
