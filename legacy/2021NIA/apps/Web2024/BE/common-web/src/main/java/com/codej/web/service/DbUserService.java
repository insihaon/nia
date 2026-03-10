package com.codej.web.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.base.dto.model.Data;
import com.codej.base.exception.CUserNotFoundException;
import com.codej.base.property.GlobalConstants;
import com.codej.base.property.GlobalConstants.CustomCacheKey;
import com.codej.web.mapper.db1st.DbUserMapper;

@Service
public class DbUserService extends BaseUserService {
    @Autowired
    private AppDto appDto;

    @Autowired
    private DbUserMapper userMapper;

    
    @Override
    public DbUserMapper getDatasource() {
        return userMapper;
    }

    @Override
    public DbUser getDefaultUser() {
        return DbUser.builder()
                .uid("dev")
                .password("dev")
                .name("dev").build();
    }

    @Override
    public BaseUser loginFromDb(String uid, String password) throws Exception {
        DbUser user = (DbUser) findUserByUid(uid);
        return user;
    }


    @Override
    @Cacheable(value = CustomCacheKey.USER, key = "#uid", unless = "#result == null")
    public DbUser findUserByUid(String uid) throws Exception {
        DbUser user = (DbUser) getUser(uid);
        return Optional.ofNullable(user).orElseThrow(CUserNotFoundException::new);
    }

    public BaseUser getUser(String uid) throws Exception {
        DbUser user = (DbUser) getDatasource().SELECT_LOGIN_USER(getUserParam(uid));
        return user;
    }

    @Override
    public DbUser findUserByUidAndProvider(String uid, String provider) throws Exception {
        return findUserByUid(uid);
    }
 

    @Override
    public HashMap<String, Object> getUserParam(String uid) {
        Data param = new Data();
        param.set(GlobalConstants.Common.UID, uid);
        return (HashMap<String, Object>) param.getMap();
    }

}
