package com.codej.web.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.DbUser;
import com.codej.base.exception.CBaseException;
import com.codej.base.property.GlobalConstants;
import com.codej.web.mapper.db1st.BaseDataMapper;
import com.codej.web.mapper.db1st.DataMapper;
import com.codej.web.security.AuthUserService;
import com.codej.web.service.BaseDataService;
import com.codej.web.service.DataService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
// @RequestMapping("/base")
public abstract class AbsDataController extends BaseDataController {
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
    public Boolean canAccess() {
        DbUser user = null;
        try {
            user = authUserService.getUser();
            log.info(user.toString());
        } catch (Exception e) {
            // 예외 처리
        }

        Boolean allowAccess = user != null;
        return allowAccess;
    }

    @Override
    protected Object getService() {
        return service;
    }

    @Override
    protected Object getMapper() {
        return mapper;
    }

    // protected abstract Object getService();

    // protected abstract Object getMapper();

    @PostConstruct
    public void Init() {
        // log.debug("DataController mappers: " + mappers.size());
        // log.debug("DataController services: " + services.size());
    }

    @Override
    protected List<Object> getServices() {
        return (List) services;
    }

    @Override
    protected List<Object> getMappers() {
        return (List) mappers;
    }

    @GetMapping(value = "/data")
    public Object getData(@RequestParam(value = "action", required = false) String action) throws CBaseException {
        return doRequest(action, new HashMap<String, Object>(), true);
    }

    @PostMapping(value = "/data")
    public Object postData(
            @RequestParam(value = "action", required = false) String action,
            @RequestBody HashMap<String, Object> param) throws CBaseException {
        return doRequest(action, param, true);
    }

    @PostMapping(value = "/selectList")
    public Object postSelectList(
            @RequestHeader(value = "command", required = false) String command,
            @RequestHeader("sqlId") String sqlId,
            @RequestBody HashMap<String, Object> param) throws CBaseException {

        HashMap<String, Object> data = nomalizeParam(param);
        Object result = selectList(command, sqlId, data, true);
        return toResult(result, (Boolean) data.getOrDefault(GlobalConstants.Common.ENCRYPT, false));
    }

    @PostMapping(value = "/selectList/{sqlId}")
    public Object postSelectList2(
            @RequestHeader(value = "command", required = false) String command,
            @PathVariable("sqlId") String sqlId,
            @RequestBody HashMap<String, Object> param) throws CBaseException {
        return postSelectList(command, sqlId, param);
    }

    @PostMapping(value = "/selectOne")
    public Object postSelectOne(
            @RequestHeader(value = "command", required = false) String command,
            @RequestHeader(value = "sqlId", required = false) String sqlId,
            @RequestBody HashMap<String, Object> param) throws CBaseException {
        HashMap<String, Object> data = nomalizeParam(param);
        Object result = selectOne(command, sqlId, data, true);
        return toResult(result, (Boolean) data.getOrDefault(GlobalConstants.Common.ENCRYPT, false));
    }

    @PostMapping(value = "/selectOne/{sqlId}")
    public Object postSelectOne2(
            @RequestHeader(value = "command", required = false) String command,
            @PathVariable("sqlId") String sqlId,
            @RequestBody HashMap<String, Object> param) throws CBaseException {
        return postSelectOne(command, sqlId, param);
    }

    @PostMapping(value = "/modify")
    public Object postModify(
            @RequestHeader(value = "command", required = false) String command,
            @RequestHeader("sqlId") String sqlId,
            @RequestBody HashMap<String, Object> param) throws CBaseException {
        HashMap<String, Object> data = nomalizeParam(param);
        Object result = modify(command, sqlId, data);
        return toResult(result, (Boolean) data.getOrDefault(GlobalConstants.Common.ENCRYPT, false));
    }

    @PostMapping(value = "/modify/{sqlId}")
    public Object postModify2(
            @RequestHeader(value = "command", required = false) String command,
            @PathVariable("sqlId") String sqlId,
            @RequestBody HashMap<String, Object> param) throws CBaseException {
        return postModify(command, sqlId, param);
    }
}
