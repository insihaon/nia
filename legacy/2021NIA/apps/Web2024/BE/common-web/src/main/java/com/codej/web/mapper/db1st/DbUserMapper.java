package com.codej.web.mapper.db1st;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.DbUser;

@Mapper
public interface DbUserMapper extends BaseUserMapper {
    public List<DbUser> selectUserList(HashMap<String, Object> map) throws Exception;

}