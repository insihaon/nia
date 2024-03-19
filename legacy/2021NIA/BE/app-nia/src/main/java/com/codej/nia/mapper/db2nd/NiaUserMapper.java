package com.codej.nia.mapper.db2nd;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.BaseUser;
import com.codej.web.mapper.db1st.BaseUserMapper;

@Mapper
public interface NiaUserMapper extends BaseUserMapper {
        public BaseUser SELECT_LOGIN_USER(HashMap<String, Object> map) throws Exception;
        public int INSERT_USER(HashMap<String, Object> user) throws Exception;
}
