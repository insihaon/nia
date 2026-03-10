package com.codej.web.mapper.db1st;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.base.dto.model.ResultMap;

@Mapper
public interface BaseUserMapper {
     public ResultMap SELECT_NOW(HashMap<String, Object> map) throws Exception;
     // 인증된 사용자를 가져올 때만 사용하도록 한다. (서비스에서 캐시하기 때문에 DB와 결과가 다를 수 있음에 주의)
     public BaseUser SELECT_LOGIN_USER(HashMap<String, Object> map) throws Exception;
     public int UPDATE_LOGIN_DATE(HashMap<String, Object> map) throws Exception;
     public int INSERT_USER(DbUser user) throws Exception;
     public int UPSERT_USER(DbUser user) throws Exception;
     public int DELETE_USER_MAP(HashMap<String, Object> map) throws Exception;
}
