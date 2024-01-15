package com.codej.web.mapper.db4th;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface Data4thMapper extends BaseDataMapper {
    public ResultMap SELECT_NOW(HashMap<String, Object> map);
}
