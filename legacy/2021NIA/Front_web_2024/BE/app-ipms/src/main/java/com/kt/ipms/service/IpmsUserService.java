package com.kt.ipms.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.codej.web.service.BaseUserService;
import com.codej.base.property.GlobalConstants;
import com.codej.base.property.GlobalConstants.CustomCacheKey;
import com.codej.base.dto.model.Data;
import com.codej.base.exception.CUserNotFoundException;
import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.kt.ipms.mapper.db2nd.IpmsUserMapper;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IpmsUserService extends BaseUserService {
    
    @Autowired
    private IpmsUserMapper ipmsUserMapper;

    
    @Override
    public IpmsUserMapper getDatasource() {
        return ipmsUserMapper;
    }

    @Override
    public BaseUser getDefaultUser() {
        return DbUser.builder()
                    .uid("dimmby")
                    .password("dimmby")
                    .name("dimmby").build();
    }

    @Override
    public TbUserBasVo loginFromDb(String uid, String password) throws Exception {
        TbUserBasVo user = findUserByUid(uid);
        return user;
    }

    @Override
    @Cacheable(value = CustomCacheKey.USER, key = "#uid", unless = "#result == null")
    public LoginInfoVo findUserByUid(String uid) throws Exception {
        LoginInfoVo user = getUser(uid);
        return Optional.ofNullable(user).orElseThrow(CUserNotFoundException::new);
    }
    

    @Override
    public LoginInfoVo getUser(String uid) throws Exception {
        LoginInfoVo searchUserBasVo = new LoginInfoVo();
        searchUserBasVo.setSuserId(uid);
        LoginInfoVo user = (LoginInfoVo)getDatasource().SELECT_LOGIN_USER(searchUserBasVo);
        return user;
    }

    @Override
    public HashMap<String, Object> getUserParam(String uid) {
        Data param = new Data();
        param.set(GlobalConstants.Common.UID, uid);
        return (HashMap<String, Object>) param.getMap();
    }
    

}
