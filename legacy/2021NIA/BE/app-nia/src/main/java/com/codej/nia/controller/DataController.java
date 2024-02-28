package com.codej.nia.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.codej.web.controller.AbsDataController;
import com.codej.web.mapper.db1st.BaseDataMapper;
import com.codej.web.mapper.db1st.DataMapper;
import com.codej.web.security.AuthUserService;
import com.codej.web.service.BaseDataService;
import com.codej.web.service.DataService;

@RestController
// @RequestMapping("/")
public class DataController extends AbsDataController {
    @Autowired
    protected DataMapper mapper;

    @Autowired
    protected DataService service;

    @Autowired
    private AuthUserService authUserService;

    @Autowired(required = false)
    protected List<BaseDataService> services;

    @Autowired(required = false)
    protected List<BaseDataMapper> mappers;

    @Override
    protected Object getService() {
        return service;
    }

    @Override
    protected Object getMapper() {
        return mapper;
    }



    @Override
    @PostMapping(value = "/selectList/{sqlId}")
    public Object postSelectList2(
            @RequestHeader(value = "command", required = false) String command,
            @PathVariable("sqlId") String sqlId,
            @RequestBody HashMap<String, Object> param) throws Exception {
        return postSelectList(command, sqlId, param);
    }

}
