package com.codej.dataHub.mapper.db2nd;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface DataHubMapper extends BaseDataMapper {
        public List<ResultMap> SELECT_API_INFO_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_API_HIST_LIST(HashMap<String, Object> map);

        public int UPDATE_API_PROC_RETRY_STATUS(HashMap<String, Object> map);


        public List<ResultMap> SELECT_API_SUCCESS_COUNT_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_API_FAIL_COUNT_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_API_HIST_AUTH_LIST(HashMap<String, Object> map);

        public int INSERT_API_AUTH_HIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_API_AUTH_HIST(HashMap<String, Object> map);

        public int UPDATE_API_AUTH_HIST(HashMap<String, Object> map);

        public int DELETE_API_AUTH_HIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_DATASET_COLUMN_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_DATA_SET_LIST(HashMap<String, Object> map);

        public int INSERT_DATA_SET_LIST(HashMap<String, Object> map);

        public int UPDATE_DATA_SET_LIST(HashMap<String, Object> map);

        public int DELETE_DATA_SET_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_DATA_TABLE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_LINK_SYSTEM_TABLE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_LINK_SYSTEM_LIST(HashMap<String, Object> map);

        public int SELECT_LINK_SYSTEM_NEXTVAL(HashMap<String, Object> map);

        public int SELECT_API_MST_NEXTVAL(HashMap<String, Object> map);

        public int SELECT_DATASET_REQ_COL_MST(HashMap<String, Object> map);

        public int INSERT_API_MST(HashMap<String, Object> map);

        public int UPDATE_API_MST(HashMap<String, Object> map);

        public int INSERT_LINK_SYSTEM_LIST(HashMap<String, Object> map);

        public int DELETE_LINK_SYSTEM_LIST(HashMap<String, Object> map);


        public List<ResultMap> SELECT_DATA_SET_HIST_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_API_DATA_SET_LIST(HashMap<String, Object> map);

        public int UPDATE_DATA_SET_HIST_LIST(HashMap<String, Object> map);

        public int DELETE_DATA_SET_HIST_LIST(HashMap<String, Object> map);

        public int INSERT_DATASET_MAPP_MST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_DATA_SET_COL_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_API_TABLE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_API_LIST(HashMap<String, Object> map);

        public int DELETE_API_TABLE_ONE(HashMap<String, Object> map);

        public List<ResultMap> SELECT_DATA_SET_POP_OVER_DATA(HashMap<String, Object> map);

        public int INSERT_DATA_SET_AUTH_LIST(HashMap<String, Object> map);

        public int INSERT_DATA_SET_VALUE_LIST(HashMap<String, Object> map);

        public int UPDATE_LINK_SYSTEM(HashMap<String, Object> map);

        public List<ResultMap> SELECT_TB_METADATA_MST(HashMap<String, Object> map);

        public List<ResultMap> ORG_HEAD_ORGS_LIST(HashMap<String, Object> map);

        public List<ResultMap> ORG_CENTER_LIST(HashMap<String, Object> map);

        public List<ResultMap> ORG_TEAM_LIST(HashMap<String, Object> map);

        public List<ResultMap> ORG_OFFICE_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_NEA_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_NEA_UPPER_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_NEA_DOWN_LIST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_TB_INTRL_SYSTEM_MST(HashMap<String, Object> map);

        public List<ResultMap> SELECT_VW_ORG_MST_HQ(HashMap<String, Object> map);

        public List<ResultMap> GET_ORG_LIST(HashMap<String, Object> map);

        public List<ResultMap> GET_OFFICE_LIST(HashMap<String, Object> map);

}
