package com.codej.web.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.base.exception.CAuthenticationException;
import com.codej.base.property.GlobalConstants.CustomCacheKey;
import com.codej.web.mapper.db1st.DbUserMapper;

@Service
public class AnonymousUserService extends BaseUserService {
    
    @Autowired 
    CacheManager cacheManager;

    HashMap<String, DbUser> mapUser = new HashMap<String, DbUser>();

    @Override
    public DbUserMapper getDatasource() {
        return null;
    }

    @Override
    public DbUser getDefaultUser() {
        return createtUser("anonymouse");
    }

    public DbUser createtUser(String uid) {
        return DbUser.builder()
                .uid(uid)
                .password(uid)
                .name(uid).build();
    }

    private void put(DbUser user) {
        mapUser.put(user.getUsername(), user);
    }

    @Override
    public DbUser loginFromDb(String uid, String password) throws Exception {
        DbUser user = (DbUser) findUserByUid(uid);
        return user;
    }


    @Override
    @Cacheable(value = CustomCacheKey.USER, key = "#uid", unless = "#result == null")
    public DbUser findUserByUid(String uid) throws CAuthenticationException {
        DbUser user = null;
		try {
			user = getUser(uid);
            if(user == null) {
                user = createtUser(uid);
                put(user);
            }
		} catch (Exception e) {
			throw new CAuthenticationException("Error in findUserByUid", e);
		}
		
		return user;
    }

    public DbUser getUser(String uid) throws Exception {
        DbUser user = mapUser.get(uid);
        return user;
    }

    @Override
    public void setUser(BaseUser user) throws Exception { 
        mapUser.put(user.getUsername(), (DbUser)user);
        SecurityContext context = SecurityContextHolder.getContext();
        org.springframework.cache.Cache cache = cacheManager.getCache(CustomCacheKey.USER);
        cache.put(context, user);
    }

    @Override
    public DbUser findUserByUidAndProvider(String uid, String provider) throws Exception {
        return findUserByUid(uid);
    }
}
