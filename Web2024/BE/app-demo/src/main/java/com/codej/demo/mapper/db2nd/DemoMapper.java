package com.codej.demo.mapper.db2nd;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;


@Mapper
public interface DemoMapper extends BaseDataMapper {
        public ResultMap SELECT_USER(HashMap<String, Object> map);
}
