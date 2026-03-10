package com.codej.nia.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codej.web.service.BaseUserService;
import com.codej.base.property.GlobalConstants;
import com.codej.base.dto.model.Data;
import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.nia.mapper.db2nd.NiaUserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NiaUserService extends BaseUserService {
    
    @Autowired
    private NiaUserMapper niaUserMapper;

    
    @Override
    public NiaUserMapper getDatasource() {
        return niaUserMapper;
    }

    @Override
    public BaseUser getDefaultUser() {
        return DbUser.builder()
                    .uid("dimmby")
                    .password("dimmby")
                    .name("dimmby").build();
    }

    @Override
    public BaseUser getUser(String uid) throws Exception {
        BaseUser user = getDatasource().SELECT_LOGIN_USER(getUserParam(uid));
        return user;
    }
    @Override
    public HashMap<String, Object> getUserParam(String uid) {
        Data param = new Data();
        param.set(GlobalConstants.Common.UID, uid);
        return (HashMap<String, Object>) param.getMap();
    }
    

}
