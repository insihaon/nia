package com.nia.korenrca.service.base;

import com.nia.korenrca.service.Service;


public abstract class BaseDbService extends Service {
    public BaseDbService(String dataSource) {
        super();
        setDataSource(dataSource);
    }
}
