package com.codej.web.mapper.db1st;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;

@Mapper
public interface MainMapper extends BaseDataMapper {
    public ResultMap SELECT_LOGIN_ID(HashMap<String, Object> map);
    public int USER_NEW_PW(HashMap<String, Object> map);
}
