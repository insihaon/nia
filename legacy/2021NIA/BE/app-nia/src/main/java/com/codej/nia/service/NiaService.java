package com.codej.nia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.codej.nia.controller.NiaApiController;
import com.codej.web.service.MainService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class NiaService extends MainService {
   
    @Autowired
    @Lazy
    protected NiaApiController NiaApiController;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    // public List<ResultMap> SELECT_API_LIST(HashMap<String, Object> param) throws Exception {
    //     param.remove("TOTAL_COUNT");
    //     List<ResultMap> resultList = niaMapper.SELECT_API_LIST(param);
    //     return resultList;
    // }
   
}
