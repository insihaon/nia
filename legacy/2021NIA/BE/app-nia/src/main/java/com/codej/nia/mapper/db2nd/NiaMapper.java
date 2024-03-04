package com.codej.nia.mapper.db2nd;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface NiaMapper extends BaseDataMapper {
        public List<ResultMap> SELECT_IP_ALARM_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_TRANSMISSION_ALARM_LIST(HashMap<String, Object> map);
}
