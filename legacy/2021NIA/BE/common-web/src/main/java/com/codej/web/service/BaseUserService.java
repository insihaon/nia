package com.codej.web.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.base.dto.model.Data;
import com.codej.base.exception.CUserNotFoundException;
import com.codej.base.property.GlobalConstants;
import com.codej.base.property.GlobalConstants.CustomCacheKey;
import com.codej.web.mapper.db1st.BaseUserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class BaseUserService {

    @Autowired
    private AppDto appDto;

    public abstract BaseUserMapper getDatasource();
    
    public abstract BaseUser getDefaultUser();

    public BaseUser loginFromDb(String uid, String password) throws Exception {
        BaseUser user = findUserByUid(uid);
        return user;
    }

    public int updateLoginDate(String uid) throws Exception {
        try {
            return getDatasource().UPDATE_LOGIN_DATE(new HashMap() {{
                put(GlobalConstants.Common.UID, uid);
            }});
            
        } catch (Exception e) {
            log.error(e.toString());
        }
        return 0;
    }

    @Cacheable(value = CustomCacheKey.USER, key = "#uid", unless = "#result == null")
    public BaseUser findUserByUid(String uid) throws Exception {
        BaseUser user = getUser(uid);
        return Optional.ofNullable(user).orElseThrow(CUserNotFoundException::new);
    }

    public abstract BaseUser getUser(String uid) throws Exception;

    public void setUser(BaseUser user) throws Exception { /* TODO: 구현 필요 */ }

    public BaseUser findUserByUidAndProvider(String uid, String provider) throws Exception {
        return findUserByUid(uid);
    }

    public void INSERT_USER(BaseUser userData) throws Exception {
        getDatasource().INSERT_USER((DbUser) userData);
    }

    public void INSERT_USER(HashMap<String, Object> userData) throws Exception { /* TODO: 구현 필요 */ }

    public void UPSERT_USER(BaseUser userData) throws Exception {
        getDatasource().UPSERT_USER((DbUser) userData);
    }

    @CacheEvict(value = CustomCacheKey.USER, key = "#uid")
    public void deleteUserByUid(String uid) throws Exception {
        getDatasource().DELETE_USER_MAP(getUserParam(uid));
    }

    public HashMap<String, Object> getUserParam(String uid) {
        Data param = new Data();
        param.set(GlobalConstants.Common.UID, uid);
        return (HashMap<String, Object>) param.getMap();
    }

    public void changeUserPassword(HashMap<String, Object> userData) throws Exception { /* TODO: 구현 필요 */ }

    private int count = 0;
    @Cacheable(value = "ECHO", key = "#uid", unless = "#result == null")
    public String getCache(String uid) throws Exception {
        return String.format("%s-%s", uid, count++);
    }

    @CacheEvict(value = "ECHO", key = "#uid")
    public void deleteCache(String uid) throws Exception { /* TODO: 구현 필요 */ }
}
