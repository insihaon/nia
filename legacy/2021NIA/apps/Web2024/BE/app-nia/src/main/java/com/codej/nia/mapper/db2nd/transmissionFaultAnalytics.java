package com.codej.nia.mapper.db2nd;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface transmissionFaultAnalytics extends BaseDataMapper {
    public List<ResultMap> SELECT_TICKET_CUR_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_TICKET_ROOT_ALARM_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_MBA_REPEATER_SLOT_DATA(HashMap<String, Object> map);

    public List<ResultMap> SELECT_MBA_LOW_ALARM_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_MBA_TOPOLOGY_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_TICKET_PMM_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_PMM_TOPOLOGY_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_MBA_PREDICTIVE_REVIEW(HashMap<String, Object> map);

    public int UPDATE_MBA_PREDICTIVE_REVIEW(HashMap<String, Object> map);

    public int DELETE_MBA_PREDICTIVE_REVIEW(HashMap<String, Object> map);

    public List<ResultMap> SELECT_TICKET_ALARM_BATCH_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_FAULTREASON_LV1_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_FAULTREASON_LV2_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_FAULTREASON_LV3_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_FAULTCHARGE_LV1_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_FAULTCHARGE_LV2_LIST(HashMap<String, Object> map);

    public List<ResultMap> SELECT_TICKET_HANDLING_CURRENT_LIST(HashMap<String, Object> map);

}
