package com.codej.web.mapper.db5th;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface Data5thMapper extends BaseDataMapper {
    public ResultMap SELECT_NOW(HashMap<String, Object> map);
}
