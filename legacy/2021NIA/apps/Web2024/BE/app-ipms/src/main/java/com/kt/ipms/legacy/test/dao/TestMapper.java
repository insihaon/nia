package com.kt.ipms.legacy.test.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;

@Mapper
public interface TestMapper {
    public ResultMap SELECT_NOW(HashMap<String, Object> map);
}
