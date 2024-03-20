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
        public List<ResultMap> SELECT_DASHBOARD_STATISTICS(HashMap<String, Object> map);
        public List<ResultMap> SELECT_SELF_PROCESS_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_SELF_PROCESS_STATISTICS(HashMap<String, Object> map);
        public List<ResultMap> SELECT_SOP_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_TRAFFIC_AGENCY_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_AGENCY_CODE_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_APP_TRAFFIC_TOP_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_APPLICATION_CODE_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_INOUT_TRAFFIC_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_UNIDENTIFIED_NATION_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_UNIDENTIFIED_AGENCY_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_UNIDENTIFIED_APP_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_EQUIP_AMOUNT_USED_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_PROFILE_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_NODE_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_PORT_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_LINK_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_AGENCY_LIST(HashMap<String, Object> map);
        public List<ResultMap> SELECT_AGENCY_IF_ID_LIST(HashMap<String, Object> map);


}
