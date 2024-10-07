package com.codej.nia.mapper.db2nd;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface NiaMapper extends BaseDataMapper {
        public List<ResultMap> SELECT_USER_LIST(HashMap<String, Object> map);

        public int UPDATE_USER_GRANT_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_IP_ALARM_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_TRANSMISSION_ALARM_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_TRANSMISSION_TICKET_DETAIL(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SYSTEM_MONITORING_CURRENT(HashMap<String, Object> map);

        public List<ResultMap> SELECT_DASHBOARD_STATISTICS(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SELF_PROCESS_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SELF_PROCESS_STATISTICS(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SELF_PROCESS_TRAFFIC_INFO(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SELF_PROCESS_SYSLOG_INFO(HashMap<String, Object> map);

        public List<ResultMap> SELECT_ATT2_CHART(HashMap<String, Object> map);

        public List<ResultMap> SELECT_NTT_CHART(HashMap<String, Object> map);

        public List<ResultMap> SELECT_AI_DETECTION_INFO(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SOP_SYSLOG_HIST_LIST(HashMap<String, Object> map);
        public int UPDATE_SYSLOG_HIST(HashMap<String, Object> map);
        public int DELETE_SYSLOG_HIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_EQUIPMENT_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_INTERFACE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SOP_HIST_LIST(HashMap<String, Object> map);
        public int UPDATE_SOP_HIST(HashMap<String, Object> map);
        public int DELETE_SOP_HIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_ALARM_CURRENT_HISTORY_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SYSLOG_HIST_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SYSLOG_EQUIPMENT_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SYSLOG_RULE_LIST(HashMap<String, Object> map);

        public int UPDATE_NIA_SYSLOG_RULE(HashMap<String, Object> map);

        public int DELETE_NIA_SYSLOG_RULE(HashMap<String, Object> map);

        public int INSERT_NIA_SYSLOG_RULE(HashMap<String, Object> map);

        public List<ResultMap> SELECT_CHECK_RULE_NAME(HashMap<String, Object> map);

        public List<ResultMap> SELECT_SOP_CODE_LIST(HashMap<String, Object> map);

        public int INSERT_SOP_CODE(HashMap<String, Object> map);

        public int UPDATE_SOP_CODE(HashMap<String, Object> map);

        public int DELETE_SOP_CODE(HashMap<String, Object> map);

        public List<ResultMap> SELECT_TRAFFIC_AGENCY_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_AGENCY_CODE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_APP_TRAFFIC_TOP_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_APPLICATION_CODE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_INOUT_TRAFFIC_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_UNIDENTIFIED_NATION_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_UNIDENTIFIED_AGENCY_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_UNIDENTIFIED_APP_LIST(HashMap<String, Object> map);

        public int INSERT_APP_IP(HashMap<String, Object> map);

        public int UPDATE_APP_IP(HashMap<String, Object> map);

        public int DELETE_APP_IP(HashMap<String, Object> map);

        public List<ResultMap> SELECT_EQUIP_AMOUNT_USED_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_PROFILE_LIST(HashMap<String, Object> map);
        
        public List<ResultMap> SELECT_MAX_PROFILE_NUM(HashMap<String, Object> map);

        public int INSERT_PROFILE_LIST(HashMap<String, Object> map);

        public int UPDATE_PROFILE_LIST(HashMap<String, Object> map);

        public int DELETE_PROFILE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_PROFILE_TICKET_TYPE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_PROFILE_ALARM_TYPE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_PROFILE_NODE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_PROFILE_RECOVERY_LIST(HashMap<String, Object> map);

        public int INSERT_PROFILE_NODE_NAME_LIST(HashMap<String, Object> map);

        public int DELETE_PROFILE_NODE_NAME_LIST(HashMap<String, Object> map);
        
        public int DELETE_PROFILE_NODE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_NODE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_PORT_LIST(HashMap<String, Object> map);

        public int UPDATE_PORT_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_LINK_LIST(HashMap<String, Object> map);

        public int INSERT_LINK_LIST(HashMap<String, Object> map);

        public int UPDATE_LINK_LIST(HashMap<String, Object> map);

        public int DELETE_LINK_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_LINK_START_NODE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_LINK_END_NODE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_LINK_IF_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_AGENCY_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_AGENCY_NODE_ID_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_AGENCY_IF_ID_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_AGENCY_IP_LIST(HashMap<String, Object> map);

        public int INSERT_AGENCY_IP_LIST(HashMap<String, Object> map);

        public int UPDATE_AGENCY_IP_LIST(HashMap<String, Object> map);

        public int DELETE_AGENCY_IP_LIST(HashMap<String, Object> map);

        public int UPDATE_AGENCY_DETAIL_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_DATA_SNAPSHOT_LIST(HashMap<String, Object> map);

        public int DELETE_DATA_SNAPSHOT(HashMap<String, Object> map);

}
