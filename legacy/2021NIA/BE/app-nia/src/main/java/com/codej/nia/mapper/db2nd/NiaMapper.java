package com.codej.nia.mapper.db2nd;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface NiaMapper extends BaseDataMapper {
        public List<ResultMap> SELECT_API_INFO_LIST(HashMap<String, Object> map);

        public int UPDATE_API_PROC_RETRY_STATUS(HashMap<String, Object> map);

}
